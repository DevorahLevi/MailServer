package com.example.mailserver.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@ApiModel
@Data
@Builder
public class Email
{
    @ApiModelProperty
    private UUID from;          // Primary Key ID of sender
    @ApiModelProperty
    private UUID to;            // Primary Key ID of recipient
    @ApiModelProperty
    private String message;     // Message content
}