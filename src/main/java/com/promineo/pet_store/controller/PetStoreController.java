package com.promineo.pet_store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.promineo.pet_store.controller.model.PetStoreData;
import com.promineo.pet_store.controller.model.PetStoreData.PetStoreCustomer;
import com.promineo.pet_store.controller.model.PetStoreData.PetStoreEmployee;
import com.promineo.pet_store.service.PetStoreService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData addPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("insertPetStore called");
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@RequestBody PetStoreData petStoreData, @PathVariable Long petStoreId) {
		log.info("Making changes to pet store {}", petStoreData);
		petStoreData.setPetStoreId(petStoreId);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@GetMapping
	public List<PetStoreData> getAllPetStores(){
		log.info("Getting list of all Stores");
		return petStoreService.getAllPetStores();
	}
	
	@GetMapping("/{storeId}")
	public PetStoreData getPetStoreById(@PathVariable Long storeId) {
		log.info("Getting petstore by ID");
		return petStoreService.getPetStoreById(storeId);
	}
	
	@DeleteMapping("/{storeId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long storeId){
		log.info("Delete called for store with id {}", storeId);
		petStoreService.deletePetStoreById(storeId);
		
		return Map.of("message", "Pet store " + storeId + " has been deleted.");
	}
	
	
	@PostMapping("{petStoreId}/employee")
	@ResponseStatus(code= HttpStatus.CREATED)
	public PetStoreEmployee addEmployeeToStore(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee petStoreEmployee){
		log.info("Adding an Employee to store {}", petStoreId);
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
	}
		
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomerToPetStore(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer petStoreCustomer) {
		log.info("Adding a Customer to store {}", petStoreId);
		
		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
	}
	
	@PutMapping("/{petStoreId}/{customerId}")
	public void addCustomerToAnotherStore(@PathVariable("petStoreId") Long petStoreId, @PathVariable("customerId") Long customerId) {
		log.info("adding an existing customer to another store");
		petStoreService.addCustomerToAnotherStore(petStoreId, customerId);
	}
}
		
	
