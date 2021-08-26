package org.example.Mapper.ES;

import org.example.Model.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

//
//import org.example.Model.DiscussPost;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
////
@Repository
public interface DiscusspostRepository extends ElasticsearchRepository<DiscussPost,Integer> {

}

