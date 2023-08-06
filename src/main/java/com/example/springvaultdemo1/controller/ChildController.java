package com.example.springvaultdemo1.controller;

import com.example.springvaultdemo1.entity.Child;
import com.example.springvaultdemo1.repo.ChildRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.Ciphertext;
import org.springframework.vault.support.Plaintext;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
		Ciphertext encryptedParent = vaultOperations.opsForTransit()
				.encrypt("nik", Plaintext.of(childReq.getParentName()));
		child.setParentName(encryptedParent.getCiphertext());
		return childRepo.save(child);
	}

	@GetMapping("get")
	Child get(@RequestParam int id){
		Child child = childRepo.getById(id);
		Plaintext decryptedParent = vaultOperations.opsForTransit()
				.decrypt("nik", Ciphertext.of(child.getParentName()));

		Child result = new Child();
		result.setId(child.getId());
		result.setName(child.getName());
		result.setParentName(decryptedParent.asString());
		return result;
	}

	@SneakyThrows
	@GetMapping("encryptFile")
	void encryptFile(){
		File file = new File("C:\\readme.pdf");
		byte[] bytes = Files.readAllBytes(file.toPath());
		Ciphertext encrypt = vaultOperations.opsForTransit()
				.encrypt("nik", Plaintext.of(bytes));
		byte[] context = encrypt.getCiphertext().getBytes(StandardCharsets.UTF_8);
		OutputStream outputStream = new FileOutputStream("C:\\readme.pdf.enc");
		outputStream.write(context);
	}

	@SneakyThrows
	@GetMapping("decryptFile")
	void decryptFile(){
		File file = new File("C:\\readme.pdf.enc");
		String string = Files.readString(file.toPath());
		Plaintext encrypt = vaultOperations.opsForTransit()
				.decrypt("nik", Ciphertext.of(string));
		byte[] context = encrypt.getPlaintext();
		OutputStream outputStream = new FileOutputStream("C:\\readme.dec.pdf");
		outputStream.write(context);
	}

}
