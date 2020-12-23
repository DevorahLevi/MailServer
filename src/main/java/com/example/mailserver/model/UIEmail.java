package com.example.mailserver.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@ApiModel
@Data
@Builder
public class UIEmail
{
    @ApiModelProperty
    private UUID from;          // UUID of sender
    @ApiModelProperty
    private String to;          // userName of recipient
    @ApiModelProperty
    private String message;     // message content
}