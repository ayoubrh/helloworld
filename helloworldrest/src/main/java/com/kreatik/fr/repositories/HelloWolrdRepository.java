package com.kreatik.fr.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.kreatik.fr.model.HelloWorld;

@RepositoryRestResource(collectionResourceRel = "hellowold", path = "api")
public interface HelloWolrdRepository extends CrudRepository<HelloWorld, Long> {


}