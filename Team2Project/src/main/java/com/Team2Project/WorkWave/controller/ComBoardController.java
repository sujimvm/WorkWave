package com.Team2Project.WorkWave.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Team2Project.WorkWave.model.CodeDTO;
import com.Team2Project.WorkWave.model.ComBoardDTO;
import com.Team2Project.WorkWave.model.ComBoardMapper;



@Controller
public class ComBoardController {

	@Autowired
	private ComBoardMapper mapper;

	@GetMapping("comboard.go")
	public String goComBoardList() {
		
		return "comBoard/list";
	}
	
	@PostMapping("comboardList.go")
	@ResponseBody
	public List<ComBoardDTO> getJobList() {
		
		return this.mapper.getJobList();
	}
	
	@PostMapping("jobCodeGroup.go")
	@ResponseBody
	public Map<String, List<CodeDTO>> getJobCodeList() {
		
		Map<String, List<CodeDTO>> map = new HashMap<>();
		
		map.put("group", this.mapper.getJobCodeGroupList());
		map.put("sub", this.mapper.getJobCodeSubList());
		map.put("step", this.mapper.getJobCodeStepList());
		
		return map;
	}

	@PostMapping("locationCodeGroup.go")
	@ResponseBody
	public Map<String, List<CodeDTO>> getLocationCodeList() {
		
		Map<String, List<CodeDTO>> map = new HashMap<>();
		
		map.put("group", this.mapper.getLocationCodeGroupList());
		map.put("sub", this.mapper.getLocationCodeSubList());
		
		return map;
	}
}
