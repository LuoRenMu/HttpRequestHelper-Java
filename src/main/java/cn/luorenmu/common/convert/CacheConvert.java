package cn.luorenmu.common.convert;

import cn.luorenmu.entiy.TaskCache;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * @author LoMu
 * Date 2024.02.18 23:37
 */
@Mapper
public interface CacheConvert {
    CacheConvert INSTANCE = Mappers.getMapper(CacheConvert.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    /**
     * @param t1 origin FF14RequestComplete
     * @param t2 new FF14RequestComplete
     *  copy t1 to t2 skip null values
     * @return merge obj
     */
    TaskCache skipNullValuesMapping(@MappingTarget TaskCache t1, TaskCache t2);

}
