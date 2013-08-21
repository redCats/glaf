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

package com.glaf.base.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.dao.AbstractSpringDao;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.query.SysTreeQuery;
import com.glaf.base.modules.sys.query.SysUserQuery;
import com.glaf.base.modules.sys.service.*;
import com.glaf.core.util.PageResult;

public class SysTreeServiceImpl implements SysTreeService {
	private static final Log logger = LogFactory
			.getLog(SysTreeServiceImpl.class);
	private AbstractSpringDao abstractDao;

	public void setAbstractDao(AbstractSpringDao abstractDao) {
		this.abstractDao = abstractDao;
		logger.info("setAbstractDao");
	}

	/**
	 * ����
	 * 
	 * @param bean
	 *            SysTree
	 * @return boolean
	 */
	public boolean create(SysTree bean) {
		boolean ret = false;
		if (abstractDao.create(bean)) {// �����¼�ɹ�
			bean.setSort((int) bean.getId());// ���������Ϊ�ղ����idֵ
			abstractDao.update(bean);
			ret = true;
		}
		return ret;
	}

	/**
	 * ����
	 * 
	 * @param bean
	 *            SysTree
	 * @return boolean
	 */
	public boolean update(SysTree bean) {
		return abstractDao.update(bean);
	}

	/**
	 * ɾ��
	 * 
	 * @param bean
	 *            SysTree
	 * @return boolean
	 */
	public boolean delete(SysTree bean) {
		// ��ȡ��ǰ�ڵ��µ������ӽڵ��б�
		List list = new ArrayList();
		getSysTree(list, (int) bean.getId(), 0);
		// Ȼ����Լ�Ҳ����ɾ���б�
		if (list != null) {
			list.add(bean);
		}

		return abstractDao.deleteAll(list);
	}

	/**
	 * ɾ��
	 * 
	 * @param id
	 *            int
	 * @return boolean
	 */
	public boolean delete(long id) {
		SysTree bean = findById(id);
		if (bean != null) {
			return delete(bean);
		} else {
			return false;
		}
	}

	/**
	 * ����ɾ��
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteAll(long[] id) {
		List list = new ArrayList();
		for (int i = 0; i < id.length; i++) {
			SysTree bean = findById(id[i]);
			if (bean != null)
				list.add(bean);
		}
		return abstractDao.deleteAll(list);
	}

	/**
	 * ��ȡ����
	 * 
	 * @param id
	 * @return
	 */
	public SysTree findById(long id) {
		return (SysTree) abstractDao.find(SysTree.class, new Long(id));
	}

	/**
	 * �����Ʋ��Ҷ���
	 * 
	 * @param name
	 *            String
	 * @return SysTree
	 */
	public SysTree findByName(String name) {
		SysTree bean = null;
		Object[] values = new Object[] { name };
		String query = "from SysTree a where a.name=? order by a.id desc";
		List list = abstractDao.getList(query, values, null);
		if (list != null && list.size() > 0) {// �м�¼
			bean = (SysTree) list.get(0);
		}
		return bean;
	}

	/**
	 * ��ȡ��ҳ�б�
	 * 
	 * @param parent
	 *            int
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return
	 */
	public PageResult getSysTreeList(int parent, int pageNo, int pageSize) {
		// ��������
		Object[] values = new Object[] { new Long(parent) };
		String query = "select count(*) from SysTree a where a.parentId=?";
		int count = ((Long) abstractDao.getList(query, values, null).iterator()
				.next()).intValue();
		if (count == 0) {// �����Ϊ��
			PageResult pager = new PageResult();
			pager.setPageSize(pageSize);
			return pager;
		}
		// ��ѯ�б�
		query = "from SysTree a where a.parentId=? order by a.sort desc";
		return abstractDao.getList(query, values, pageNo, pageSize, count);
	}

	/**
	 * ��ȡȫ���б�
	 * 
	 * @param parent
	 *            int
	 * @return List
	 */
	public List getSysTreeList(int parent) {
		// ��������
		Object[] values = new Object[] { new Long(parent) };
		String query = "from SysTree a where a.parentId=? order by a.sort desc";
		return abstractDao.getList(query, values, null);
	}

	/**
	 * ��ȡȫ���б�
	 * 
	 * @param parent
	 *            int ��ID
	 * @param status
	 *            int ״̬
	 * @return List
	 */
	public List getSysTreeListForDept(int parent, int status) {
		// ��������
		Object[] values = new Object[] { new Long(parent) };
		String query = "from SysTree a where a.parentId=? ";
		if (status != -1) {
			query += " and a.department.status = " + status;
		}
		query += "	order by a.sort desc";
		return abstractDao.getList(query, values, null);
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @param parent
	 *            int
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public void getSysTrees(List tree, int parent, int deep) {
		// ���Ȼ�ȡ��ǰ�ڵ��µ������ӽڵ�
		Object[] values = new Object[] { new Long(parent) };
		String query = "from SysTree a where a.parentId=? order by a.sort desc";
		List nodes = abstractDao.getList(query, values, null);
		if (nodes != null) {
			Iterator iter = nodes.iterator();
			while (iter.hasNext()) {// �ݹ����
				SysTree bean = (SysTree) iter.next();
				bean.setDeep(deep + 1);
				tree.add(bean);// ���뵽����
				getSysTrees(tree, (int) bean.getId(), bean.getDeep());
			}
		}
	}

	/**
	 * ��ȡ���ڵ��б�����:��Ŀ¼>A>A1>A11
	 * 
	 * @param tree
	 * @param int id
	 */
	public void getSysTreeParent(List tree, long id) {
		// �����Ƿ��и��ڵ�
		SysTree bean = findById(id);
		if (bean != null) {
			if (bean.getParentId() != 0) {
				getSysTreeParent(tree, bean.getParentId());
			}
			tree.add(bean);
		}
	}
	
	public List<SysTree> getAllSysTreeListForDeptNoSort(long parentId, int status){
		return null;
	}

	/**
	 * ����
	 * 
	 * @param bean
	 *            SysTree
	 * @param operate
	 *            int ����
	 */
	public void sort(long parent, SysTree bean, int operate) {
		if (operate == SysConstants.SORT_PREVIOUS) {// ǰ��
			sortByPrevious(parent, bean);
		} else if (operate == SysConstants.SORT_FORWARD) {// ����
			sortByForward(parent, bean);
		}
	}

	/**
	 * ��ǰ�ƶ�����
	 * 
	 * @param bean
	 */
	private void sortByPrevious(long parent, SysTree bean) {
		Object[] values = new Object[] { new Long(parent),
				new Integer(bean.getSort()) };
		// ����ǰһ������
		String query = "from SysTree a where a.parentId=? and a.sort>? order by a.sort asc";
		List list = abstractDao.getList(query, values, null);
		if (list != null && list.size() > 0) {// �м�¼
			SysTree temp = (SysTree) list.get(0);
			int i = bean.getSort();
			bean.setSort(temp.getSort());
			abstractDao.update(bean);// ����bean

			temp.setSort(i);
			abstractDao.update(temp);// ����temp
		}
	}

	/**
	 * ����ƶ�����
	 * 
	 * @param bean
	 */
	private void sortByForward(long parent, SysTree bean) {
		Object[] values = new Object[] { new Long(parent),
				new Integer(bean.getSort()) };
		// ���Һ�һ������
		String query = "from SysTree a where a.parentId=? and a.sort<? order by a.sort desc";
		List list = abstractDao.getList(query, values, null);
		if (list != null && list.size() > 0) {// �м�¼
			SysTree temp = (SysTree) list.get(0);
			int i = bean.getSort();
			bean.setSort(temp.getSort());
			abstractDao.update(bean);// ����bean

			temp.setSort(i);
			abstractDao.update(temp);// ����temp
		}
	}

	/**
	 * ������Ż�ȡ���ڵ�
	 * 
	 * @param tree
	 * @return SysTree
	 */
	public SysTree getSysTreeByCode(String code) {
		SysTree bean = null;
		Object[] values = new Object[] { code };
		// ���Һ�һ������
		String query = "from SysTree a where a.code=?";
		List list = abstractDao.getList(query, values, null);
		if (list != null && list.size() > 0) {// �м�¼
			bean = (SysTree) list.get(0);
		}
		return bean;
	}

	public List<SysTree> getAllSysTreeList() {

		return null;
	}

	public List<SysTree> getAllSysTreeListForDept(long parent, int status) {

		return null;
	}

	public List<SysTree> getApplicationSysTrees(SysTreeQuery query) {

		return null;
	}

	public List<SysTree> getDepartmentSysTrees(SysTreeQuery query) {

		return null;
	}

	public List<SysTree> getDictorySysTrees(SysTreeQuery query) {

		return null;
	}

	public List<SysTree> getRelationSysTrees(String relationTable,
			String relationColumn, SysTreeQuery query) {

		return null;
	}

	public List<SysTree> getRoleUserTrees(SysUserQuery query) {

		return null;
	}

	public void getSysTree(List<SysTree> tree, long parentId, int deep) {

	}

	public int getSysTreeCountByQueryCriteria(SysTreeQuery query) {

		return 0;
	}

	public List<SysTree> getSysTreeList(long parentId) {

		return null;
	}

	public PageResult getSysTreeList(long parentId, int pageNo, int pageSize) {

		return null;
	}

	public List<SysTree> getSysTreeListForDept(long parentId, int status) {

		return null;
	}

	public List<SysTree> getSysTreesByQueryCriteria(int start, int pageSize,
			SysTreeQuery query) {

		return null;
	}

	public void updateTreeIds() {

	}

	public void updateTreeIds(Map<Long, String> treeMap) {

	}
}