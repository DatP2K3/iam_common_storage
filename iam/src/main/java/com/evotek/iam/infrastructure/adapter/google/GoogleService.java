package com.evotek.iam.infrastructure.adapter.google;

import com.evotek.iam.application.dto.request.ExchangeTokenRequest;
import com.evotek.iam.application.dto.response.OutboundUserDTO;
import com.evotek.iam.application.dto.response.TokenDTO;

public interface GoogleService {
    TokenDTO exchangeToken(ExchangeTokenRequest request);

    OutboundUserDTO getUserInfo(String accessToken);
}
