package com.example.mailserver.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class DisplayInboxEmail
{
    @ApiModelProperty
    private String from;        // Username of sender
    @ApiModelProperty
    private String message;     // Message content
}
