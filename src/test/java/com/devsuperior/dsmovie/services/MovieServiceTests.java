package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;

	@Mock
	private MovieRepository repository;

	private MovieDTO movieDTO;
	private PageImpl<MovieEntity> page;
	private Long existingMovieId, nonExistingMovieId, referenceMovieId;
	private MovieEntity movieEntity;

	@BeforeEach
	public void setUp() throws Exception {
		existingMovieId = 1L;
		nonExistingMovieId = 1000L;
		referenceMovieId = 4L;

		movieDTO = MovieFactory.createMovieDTO();

		movieEntity = MovieFactory.createMovieEntity();

		page = new PageImpl<>(List.of(movieEntity));
		Mockito.when(repository.searchByTitle(any(), any())).thenReturn(page);
		Mockito.when(repository.findById(existingMovieId)).thenReturn(java.util.Optional.of(movieEntity));
		Mockito.when(repository.findById(nonExistingMovieId)).thenReturn(java.util.Optional.empty());
		Mockito.when(repository.save(any())).thenReturn(movieEntity);
		Mockito.when(repository.getReferenceById(existingMovieId)).thenReturn(movieEntity);
		Mockito.when(repository.getReferenceById(nonExistingMovieId)).thenThrow(EntityNotFoundException.class);
		Mockito.doNothing().when(repository).deleteById(existingMovieId);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).deleteById(nonExistingMovieId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(referenceMovieId);
		Mockito.when(repository.existsById(existingMovieId)).thenReturn(true);
		Mockito.when(repository.existsById(referenceMovieId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingMovieId)).thenReturn(false);
	}
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		Pageable pageable = PageRequest.of(0, 12);
		Page<MovieDTO> result = service.findAll(movieDTO.getTitle(), pageable);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.findById(existingMovieId);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingMovieId, result.getId());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingMovieId);
		});
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
		MovieDTO result = service.insert(movieDTO);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.update(existingMovieId, movieDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingMovieId, result.getId());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingMovieId, movieDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		service.delete(existingMovieId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingMovieId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingMovieId);
		});
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(referenceMovieId);
		});
	}
}
