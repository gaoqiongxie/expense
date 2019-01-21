package com.xw.restful.dao;

import java.util.List;

import com.xw.restful.domain.FmlMember;

public interface FmlMemberDao {

	List<FmlMember> fmlMembers();

	int replaceInto(String memberName);

}
