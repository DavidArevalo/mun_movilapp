package ec.gob.loja.movilapp.service.mapper;

import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.domain.FrequentlyQuestion;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import ec.gob.loja.movilapp.service.dto.FrequentlyQuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FrequentlyQuestion} and its DTO {@link FrequentlyQuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface FrequentlyQuestionMapper extends EntityMapper<FrequentlyQuestionDTO, FrequentlyQuestion> {
    @Mapping(target = "application", source = "application", qualifiedByName = "applicationId")
    FrequentlyQuestionDTO toDto(FrequentlyQuestion s);

    @Named("applicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationDTO toDtoApplicationId(Application application);
}
