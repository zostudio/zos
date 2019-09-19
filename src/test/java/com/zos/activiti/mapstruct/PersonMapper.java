package com.zos.activiti.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.zos.activiti.bo.PersonBO;
import com.zos.activiti.dto.PersonDTO;

@Mapper
public interface PersonMapper {
	
	PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
	
	@Mappings({
        @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd HH:mm:ss")
	})
	public PersonDTO boToDto(PersonBO personBo);
	
	@Mappings({
        @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd HH:mm:ss")
	})
	public PersonBO dtoToBo(PersonDTO personDto);
	
	public List<PersonDTO> boToDto(List<PersonBO> personBos);
	
	public List<PersonBO> dtoToBo(List<PersonDTO> personDtos);
}
