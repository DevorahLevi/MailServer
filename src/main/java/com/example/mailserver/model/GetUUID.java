package com.example.mailserver.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

@ApiModel
@Data
public class GetUUID
{
    @ApiModelProperty
    private UUID primaryKey;
}