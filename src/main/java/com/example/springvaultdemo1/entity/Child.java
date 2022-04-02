package com.example.springvaultdemo1.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/************************
 * Made by [MR Ferry™]  *
 * on April 2022        *
 ************************/

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Child{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String name;
	String parentName;

	@Override
	public boolean equals(Object obj){
		if(this == obj) return true;
		if(obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
		Child child = (Child) obj;
		return id != null && Objects.equals(id, child.id);
	}

	@Override
	public int hashCode(){
		return getClass().hashCode();
	}
}
