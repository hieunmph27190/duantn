/*
 * (C) Copyright 2022. All Rights Reserved.
 *
 * @author DongTHD
 * @date Mar 10, 2022
*/
package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorite implements Serializable{


	@Id
	@Column(name = "favorite_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long favoriteId;


	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;


	@ManyToOne()
	@JoinColumn(name = "customer_id")
	private Customer customer;



}
