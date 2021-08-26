package org.example.service;

import org.example.Mapper.ProblemMapper;
import org.example.Model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemShowService {
    @Autowired
    ProblemMapper problemMapper;

    public Problem selectByOne(int id){

        return problemMapper.SelectOneProblem(id);

    }

    public List<Problem> SelectAll(int strart,int limit){
        return  problemMapper.SelectAllProblem(strart,limit);
    }


    public int selectAllProblemCount() {
        
        return problemMapper.selectAllProblemCount();
    }
}
