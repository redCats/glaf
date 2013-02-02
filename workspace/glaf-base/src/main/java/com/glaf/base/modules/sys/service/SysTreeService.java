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

package com.glaf.base.modules.sys.service;

import java.util.List;

import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.utils.PageResult;

public interface SysTreeService {

	/**
	 * 保存
	 * 
	 * @param bean
	 *            SysTree
	 * @return boolean
	 */
	boolean create(SysTree bean);

	/**
	 * 删除
	 * 
	 * @param id
	 *            int
	 * @return boolean
	 */
	boolean delete(long id);

	/**
	 * 删除
	 * 
	 * @param bean
	 *            SysTree
	 * @return boolean
	 */
	boolean delete(SysTree bean);

	/**
	 * 批量删除
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteAll(long[] id);

	/**
	 * 获取对象
	 * 
	 * @param id
	 * @return
	 */
	SysTree findById(long id);

	/**
	 * 按名称查找对象
	 * 
	 * @param name
	 *            String
	 * @return SysTree
	 */
	SysTree findByName(String name);

	/**
	 * 获取树型列表
	 * 
	 * @param parent
	 *            int
	 * @return List
	 */
	void getSysTree(List<SysTree> tree, int parent, int deep);

	/**
	 * 按树编号获取树节点
	 * 
	 * @param tree
	 * @return SysTree
	 */
	SysTree getSysTreeByCode(String code);

	/**
	 * 获取全部列表
	 * 
	 * @param parent
	 *            int
	 * @return List
	 */
	List<SysTree> getSysTreeList(int parent);

	/**
	 * 获取分页列表
	 * 
	 * @param parent
	 *            int
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return
	 */
	PageResult getSysTreeList(int parent, int pageNo, int pageSize);

	/**
	 * 获取全部列表
	 * 
	 * @param parent
	 *            int 父ID
	 * @param status
	 *            int 状态
	 * @return List
	 */
	List<SysTree> getSysTreeListForDept(int parent, int status);

	/**
	 * 获取父节点列表，如:根目录>A>A1>A11
	 * 
	 * @param tree
	 * @param int id
	 */
	void getSysTreeParent(List<SysTree> tree, long id);

	/**
	 * 排序
	 * 
	 * @param bean
	 *            SysTree
	 * @param operate
	 *            int 操作
	 */
	void sort(long parent, SysTree bean, int operate);

	/**
	 * 更新
	 * 
	 * @param bean
	 *            SysTree
	 * @return boolean
	 */
	boolean update(SysTree bean);
}