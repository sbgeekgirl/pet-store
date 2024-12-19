package com.promineo.pet_store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.promineo.pet_store.controller.model.PetStoreData;
import com.promineo.pet_store.controller.model.PetStoreData.PetStoreCustomer;
import com.promineo.pet_store.controller.model.PetStoreData.PetStoreEmployee;
import com.promineo.pet_store.dao.CustomerDao;
import com.promineo.pet_store.dao.EmployeeDao;
import com.promineo.pet_store.dao.PetStoreDao;
import com.promineo.pet_store.entity.Customer;
import com.promineo.pet_store.entity.Employee;
import com.promineo.pet_store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;
	
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}
	
	private PetStore findOrCreatePetStore(Long petStoreId) {
		if(Objects.isNull(petStoreId)) {
			return new PetStore();
		}
		else {
			return findPetStoreById(petStoreId);
		}
	}
	
	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		
	}
	
	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(() -> 
			new NoSuchElementException("A pet store with that ID cannot be found"));
	}
	
	public PetStoreData updatePetStore(Long petStoreId, PetStoreData petStoreData) {
		PetStore petStore = findPetStoreById(petStoreId);
		copyPetStoreFields(petStore, petStoreData);
		System.out.println(petStoreData);
		return petStoreData;
	}
	
	@Transactional(readOnly = true)
	public List<PetStoreData> getAllPetStores(){
		List<PetStoreData> result = new LinkedList<>();
		List<PetStore> petStores = petStoreDao.findAll();
		
		for(PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			result.add(psd);
		}
		
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public PetStoreData getPetStoreById(Long petStoreId) {
		return new PetStoreData(findPetStoreById(petStoreId));
	}

	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}

	
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {

		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		Employee dbEmployee = employeeDao.save(employee);
		
		return new PetStoreEmployee(dbEmployee);
	}
	
	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if(Objects.isNull(employeeId)) {
			return new Employee();
		}
		else {
			return findEmployeeById(petStoreId, employeeId);
		}
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		
	}
	
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(() -> 
			new NoSuchElementException("Employee with that Id was not found"));
		if(employee.getPetStore().getPet_store_id() != petStoreId) {
			throw new IllegalArgumentException("That employee does not work for that store");
		}
		return employee;
	}

	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		copyCustomerFields(customer, petStoreCustomer);
		
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		Customer dbCustomer = customerDao.save(customer);
		
		return new PetStoreCustomer(dbCustomer);
	}
	
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {
			return new Customer();
		}
		else {
			return findCustomerById(petStoreId, customerId);
		}
	}
	
	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		
	}
	
	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(() -> 
			new NoSuchElementException("Customer with that Id was not found"));
		
		boolean found = false;
		
		for(PetStore petStore : customer.getPetStores()) {
			if(petStore.getPet_store_id() == petStoreId){
				found = true;
				break;
			}
			if(!found) {
				throw new IllegalArgumentException("That customer does not shop at that store");
			}
		}
			
		return customer;
	}
	
	@Transactional(readOnly = false)
	public void addCustomerToAnotherStore(Long storeId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(() -> 
			new NoSuchElementException("Customer with that Id was not found"));
		PetStore petStore = findPetStoreById(storeId);
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		
		
		
	}
}
