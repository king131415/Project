package org.example.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.Model.Problem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProblemMapper {
//order by type desc ,create_time desc
//       limit #{start}, #{limit}
    @Select({
            "select * from problem_table order by id asc " +
                    "limit #{strart}, #{limit}"
    })
    List<Problem> SelectAllProblem(int strart, int limit);
    
    
    @Select({
            "select count(id)  from problem_table"
    })
    int selectAllProblemCount();


    @Select({
            "select id,title,level,description,templateCode from problem_table where id=#{id}"
    })
    Problem  SelectOneProblem(int id);


}
