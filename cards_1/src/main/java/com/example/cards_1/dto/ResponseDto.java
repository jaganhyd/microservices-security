package com.example.cards_1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

	private String statusCode;
	private String statusMsg;

}
