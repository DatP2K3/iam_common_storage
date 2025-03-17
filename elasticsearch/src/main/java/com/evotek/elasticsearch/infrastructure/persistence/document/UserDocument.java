package com.evotek.elasticsearch.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.util.UUID;

@Document(indexName = "user")
@Setting(settingPath = "esconfig/elastic-analyzer.json")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private UUID selfUserID;
    @Field(type = FieldType.Keyword)
    private UUID providerId;
    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search"),
            otherFields = {
                    @InnerField(suffix = "sort", type = FieldType.Keyword)
            }
    )
    private String username;
    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search"),
            otherFields = {
                    @InnerField(suffix = "sort", type = FieldType.Keyword)
            }
    )
    private String email;
    @Field(type = FieldType.Keyword)
    private String firstName;
    @Field(type = FieldType.Keyword)
    private String lastName;
    @Field(type = FieldType.Keyword)
    private UUID avatarFileId;
    @Field(type = FieldType.Date)
    private LocalDate dob;
    @Field(type = FieldType.Keyword)
    private String street;
    @Field(type = FieldType.Keyword)
    private String ward;
    @Field(type = FieldType.Keyword)
    private String district;
    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search"),
            otherFields = {
                    @InnerField(suffix = "sort", type = FieldType.Keyword)
            }
    )
    private String city;
    @Field(type = FieldType.Integer)
    private int yearsOfExperience;
    @Field(type = FieldType.Keyword)
    private boolean locked;
    @Field(type = FieldType.Keyword)
    private String provider;
    @Field(type = FieldType.Keyword)
    private UUID roleId;
}
