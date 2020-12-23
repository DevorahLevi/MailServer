package com.example.mailserver.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class DisplayOutboxEmail
{
    @ApiModelProperty
    private String to;          // Username of recipient
    @ApiModelProperty
    private String message;     // Message content
}
