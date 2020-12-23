package com.example.mailserver.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class ExternalEmail
{
    @ApiModelProperty
    private String from;    // sender user
    @ApiModelProperty
    private String to;      // recipient user
    @ApiModelProperty
    private String message;
}