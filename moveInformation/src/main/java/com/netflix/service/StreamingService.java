package com.netflix.service;

import com.netflix.entity.StreamingEntity;
import com.netflix.dto.StreamingDTO;
import com.netflix.repository.StreamingRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
@Transactional(readOnly = true)
public class StreamingService {
    private final StreamingRepository streamingRepository;

    @Transactional
    public StreamingDTO getStreamingId(long showId) {
        StreamingEntity movie = streamingRepository.findById(showId).orElseThrow();
        return StreamingDTO.fromDTO(movie);
    }

    @Transactional
    public Page<StreamingDTO> getStreamingAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StreamingEntity> pageMovie = streamingRepository.findAll(pageable);
        List<StreamingDTO> listData = pageMovie.getContent().stream().map(StreamingDTO::fromDTO).toList();

        return new PageImpl<>(listData, pageable, pageMovie.getTotalElements());
    }

    @Transactional
    public List<StreamingDTO> getStreamingSearch(String title, String duration, String typee, String releaseYear) {
        if(title == null && duration == null && typee == null && releaseYear == null)
            return streamingRepository.findTop50By().stream().map(StreamingDTO::fromDTO).toList();

        Specification<StreamingEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> listPredicate = new ArrayList<>();

            if (title != null)
                listPredicate.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            if (duration != null)
                listPredicate.add(criteriaBuilder.equal(root.get("duration"), duration));
            if (typee != null)
                listPredicate.add(criteriaBuilder.equal(root.get("typee"), typee));
            if(releaseYear != null)
                listPredicate.add(criteriaBuilder.equal(root.get("releaseYear"), releaseYear));

            return criteriaBuilder.and(listPredicate.toArray(new Predicate[0]));
        };
        return streamingRepository.findAll(specification).stream().map(StreamingDTO::fromDTO).toList();
    }

    @Transactional
    public void setMovie(StreamingDTO movieDTO) {
        StreamingEntity movie = new StreamingEntity();
        movie.fromEntity(movieDTO);
        streamingRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(long showId) {
        streamingRepository.deleteById(showId);
    }

    @Transactional
    public void updateMovie(long showId, StreamingDTO movieDTO) {
        StreamingEntity movie = streamingRepository.findById(showId).orElseThrow();
        movie.fromEntity(movieDTO);
    }
}
