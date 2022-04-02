package com.example.springvaultdemo1.controller;

import com.example.springvaultdemo1.entity.Child;
import com.example.springvaultdemo1.repo.ChildRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.Ciphertext;
import org.springframework.vault.support.Plaintext;
import org.springframework.web.bind.annotation.*;

/************************
 * Made by [MR Ferry™]  *
 * on April 2022        *
 ************************/

@RestController
@RequiredArgsConstructor
public class ChildController{
	private final ChildRepo childRepo;
	private final VaultOperations vaultOperations;

	@PostMapping("save")
	Child save(@RequestBody Child childReq){
		Child child = new Child();
		child.setName(childReq.getName());
		Ciphertext encryptedParent = vaultOperations.opsForTransit().encrypt("nik", Plaintext.of(childReq.getParentName()));
		child.setParentName(encryptedParent.getCiphertext());
		return childRepo.save(child);
	}

	@GetMapping("get")
	Child get(@RequestParam int id){
		Child child = childRepo.getById(id);
		Plaintext decryptedParent = vaultOperations.opsForTransit().decrypt("nik", Ciphertext.of(child.getParentName()));

		Child result = new Child();
		result.setId(child.getId());
		result.setName(child.getName());
		result.setParentName(decryptedParent.asString());
		return result;
	}

}
