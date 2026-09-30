package com.example.accounts_1.dto;

//generates the getter for all fields
//fields as final
public record AccountsMsgDto(Long accountNumber, String name, String email, String mobileNumber) {

}
