package com.btb.chalKak.global.mapper;

import com.btb.chalKak.domain.post.dto.PostDto;
import com.btb.chalKak.domain.post.entity.Post;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDto, Post> {

    PostMapper MAPPER = Mappers.getMapper(PostMapper.class);

    Post toEntity(final PostDto postDto);

    PostDto toDto(final Post post);

    List<PostDto> toDtoList(List<Post> Posts);

    List<Post> toEntityList(List<PostDto> PostDtos);

}
