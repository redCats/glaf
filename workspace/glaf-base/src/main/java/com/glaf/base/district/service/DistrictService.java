/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.base.district.service;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.TreeModel;
import com.glaf.core.query.TreeModelQuery;
import com.glaf.base.district.domain.*;
import com.glaf.base.district.query.*;

@Transactional(readOnly = true)
public interface DistrictService {
	
	
	void deleteById(long id);

	/**
	 * ����������ȡһ����¼
	 * 
	 * @return
	 */
	DistrictEntity getDistrict(Long id);

	/**
	 * ���ݲ�ѯ������ȡ��¼����
	 * 
	 * @return
	 */
	int getDistrictCountByQueryCriteria(DistrictQuery query);

	/**
	 * ���ݲ�ѯ������ȡһҳ������
	 * 
	 * @return
	 */
	List<DistrictEntity> getDistrictsByQueryCriteria(int start, int pageSize,
			DistrictQuery query);

	int getDistrictTreeModelCount(TreeModelQuery query);

	List<TreeModel> getDistrictTreeModels(TreeModelQuery query);

	/**
	 * ���ݲ�ѯ������ȡ��¼�б�
	 * 
	 * @return
	 */
	List<DistrictEntity> list(DistrictQuery query);
	
	List<DistrictEntity>  getDistrictList(long parentId);

	/**
	 * ����һ����¼
	 * 
	 * @return
	 */
	@Transactional
	void save(DistrictEntity district);

}