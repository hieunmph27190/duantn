package com.fpt.duantn.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserInfoResponse {
	private UUID id;
	private String email;
	private List<String> roles;

	public UserInfoResponse(UUID id, String email, List<String> roles) {
		this.id = id;
		this.email = email;
		this.roles = roles;
	}

}
