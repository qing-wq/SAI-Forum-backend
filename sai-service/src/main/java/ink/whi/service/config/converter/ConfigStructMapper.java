package ink.whi.service.config.converter;

import ink.whi.api.model.vo.config.dto.GlobalConfigDTO;
import ink.whi.api.model.vo.config.req.GlobalConfigReq;
import ink.whi.api.model.vo.config.req.SearchGlobalConfigReq;
import ink.whi.service.config.repo.entity.GlobalConfigDO;
import ink.whi.service.config.repo.params.SearchGlobalConfigParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: qing
 * @Date: 2024/8/29
 */
@Mapper
public interface ConfigStructMapper {
    // instance
    ConfigStructMapper INSTANCE = Mappers.getMapper( ConfigStructMapper.class );

    // req to params
    @Mapping(source = "pageNumber", target = "pageNum")
    @Mapping(source = "keywords", target = "key")
    SearchGlobalConfigParams toSearchGlobalParams(SearchGlobalConfigReq req);


    // do to dto
    @Mapping(source = "key", target = "keywords")
    GlobalConfigDTO toGlobalDTO(GlobalConfigDO configDO);

    List<GlobalConfigDTO> toGlobalDTOS(List<GlobalConfigDO> configDOS);

    // req to do
    @Mapping(source = "keywords", target = "key")
    GlobalConfigDO toGlobalDO(GlobalConfigReq req);
}
