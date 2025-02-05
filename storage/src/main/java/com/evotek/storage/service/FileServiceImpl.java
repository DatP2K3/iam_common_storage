package com.evotek.storage.service;

import com.evo.common.dto.response.FileResponse;
import com.evotek.storage.config.FileStorageProperties;
import com.evotek.storage.dto.request.FileSearchRequest;
import com.evotek.storage.mapper.FileMapper;
import com.evotek.storage.model.File;
import com.evotek.storage.repository.FileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private Path publicStorageLocation;
    private Path privateStorageLocation;
    private final FileStorageProperties fileStorageProperties;

    @PostConstruct
    public void init() {
        try {
            this.publicStorageLocation = Paths.get(fileStorageProperties.getPublicUploadDir())
                    .toAbsolutePath().normalize();
            this.privateStorageLocation = Paths.get(fileStorageProperties.getPrivateUploadDir())
                    .toAbsolutePath().normalize();

            Files.createDirectories(publicStorageLocation);
            Files.createDirectories(privateStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directories for storing files.", ex);
        }
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image");
    }

    private String hashFileName(String fileName) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = messageDigest.digest(fileName.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing file name", e);
        }
    }

    public void validateFile(MultipartFile file) {
        List<String> allowedMimeTypes = Arrays.asList(fileStorageProperties.getAllowedTypes().split(","));
        List<String> allowedExtensions = Arrays.asList(fileStorageProperties.getAllowedExtensions().split(","));

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(fileName);

        if (contentType == null || !allowedMimeTypes.contains(contentType)) {
            throw new RuntimeException("File type not supported: " + contentType);
        }

        if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
            throw new RuntimeException("File extension not supported: " + fileExtension);
        }
    }

    @Override
    public List<FileResponse> storeFile(List<MultipartFile> files, boolean isPublic, String description) {
        List<FileResponse> fileResponses = new ArrayList<>();
        for(MultipartFile file : files) {
            try {
                validateFile(file);
                String originName = file.getOriginalFilename();
                if (originName == null || originName.contains("..")) {
                    throw new RuntimeException("Invalid file name: " + originName);
                }
                String fileExtension = originName.substring(originName.lastIndexOf("."));
                String md5Name = hashFileName(originName) + fileExtension;

                Path storageLocation = isPublic ? publicStorageLocation : privateStorageLocation;
                Path targetLocation = storageLocation.resolve(md5Name);
                Integer fileWidth = null;
                Integer fileHeight = null;
                if (isImage(file)) {
                    BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                    fileWidth = bufferedImage.getWidth();
                    fileHeight = bufferedImage.getHeight();
                }
                file.transferTo(targetLocation.toFile());
                File fileEntity = File.builder()
                        .originName(originName)
                        .md5Name(md5Name)
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .fileWidth(fileWidth)
                        .fileHeight(fileHeight)
                        .description(description)
                        .isPublic(isPublic)
                        .build();
                File savedFile = fileRepository.save(fileEntity);
                if (isPublic) {
                    String url = "http://localhost:8080/api/public/file/" + savedFile.getId();
                    savedFile.setUrl(url);
                } else {
                    String url = "http://localhost:8080/api/file/" + savedFile.getId();
                    savedFile.setUrl(url);
                }
                savedFile = fileRepository.save(savedFile);
                fileResponses.add(fileMapper.fileToFileResponse(savedFile));
            } catch (IOException e) {
                throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
            }
        }
        return fileResponses;
    }

    @Override
    public FileResponse updateFile(int fieldId, String fileName, String description) {
        File file = fileRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + fieldId));
        file.setOriginName(fileName);
        file.setDescription(description);
        File updatedFile = fileRepository.save(file);
        return fileMapper.fileToFileResponse(updatedFile);
    }

    @Override
    public void deleteFile(int fileId) {
        fileRepository.deleteById(fileId);
    }

    @Override
    public List<FileResponse> search(FileSearchRequest fileSearchRequest) {
        List<File> files = fileRepository.search(fileSearchRequest);
        return files.stream().map(fileMapper::fileToFileResponse).toList();
    }

    @Override
    public FileResponse getPublicFile(int filedId) {
        File file = fileRepository.findById(filedId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + filedId));
        String url = "http://localhost:8080/api/uploads/public/" + file.getMd5Name();
        FileResponse fileResponse = fileMapper.fileToFileResponse(file);
        fileResponse.setUrl(url);
        return fileResponse;
    }

    @Override
    public FileResponse getPrivateFile(int filedId) {
        File file = fileRepository.findById(filedId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + filedId));
        String url = "http://localhost:8080/api/uploads/private/" + file.getMd5Name();
        FileResponse fileResponse = fileMapper.fileToFileResponse(file);
        fileResponse.setUrl(url);
        return fileResponse;
    }
}
