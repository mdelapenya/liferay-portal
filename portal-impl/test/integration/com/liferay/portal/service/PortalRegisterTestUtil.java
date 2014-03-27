/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service;

import com.liferay.portal.asset.LayoutRevisionAssetRendererFactory;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.assetpublisher.util.AssetIndexer;
import com.liferay.portlet.blogs.asset.BlogsEntryAssetRendererFactory;
import com.liferay.portlet.blogs.trash.BlogsEntryTrashHandler;
import com.liferay.portlet.blogs.util.BlogsIndexer;
import com.liferay.portlet.blogs.workflow.BlogsEntryWorkflowHandler;
import com.liferay.portlet.bookmarks.asset.BookmarksEntryAssetRendererFactory;
import com.liferay.portlet.bookmarks.asset.BookmarksFolderAssetRendererFactory;
import com.liferay.portlet.bookmarks.util.BookmarksEntryIndexer;
import com.liferay.portlet.bookmarks.util.BookmarksFolderIndexer;
import com.liferay.portlet.directory.asset.UserAssetRendererFactory;
import com.liferay.portlet.directory.workflow.UserWorkflowHandler;
import com.liferay.portlet.documentlibrary.asset.DLFileEntryAssetRendererFactory;
import com.liferay.portlet.documentlibrary.asset.DLFolderAssetRendererFactory;
import com.liferay.portlet.documentlibrary.trash.DLFileEntryTrashHandler;
import com.liferay.portlet.documentlibrary.trash.DLFileShortcutTrashHandler;
import com.liferay.portlet.documentlibrary.trash.DLFolderTrashHandler;
import com.liferay.portlet.documentlibrary.util.DLFileEntryIndexer;
import com.liferay.portlet.documentlibrary.util.DLFolderIndexer;
import com.liferay.portlet.documentlibrary.workflow.DLFileEntryWorkflowHandler;
import com.liferay.portlet.dynamicdatalists.asset.DDLRecordAssetRendererFactory;
import com.liferay.portlet.dynamicdatalists.util.DDLIndexer;
import com.liferay.portlet.dynamicdatalists.workflow.DDLRecordWorkflowHandler;
import com.liferay.portlet.journal.asset.JournalArticleAssetRendererFactory;
import com.liferay.portlet.journal.asset.JournalFolderAssetRendererFactory;
import com.liferay.portlet.journal.trash.JournalArticleTrashHandler;
import com.liferay.portlet.journal.util.JournalArticleIndexer;
import com.liferay.portlet.journal.util.JournalFolderIndexer;
import com.liferay.portlet.journal.workflow.JournalArticleWorkflowHandler;
import com.liferay.portlet.messageboards.asset.MBCategoryAssetRendererFactory;
import com.liferay.portlet.messageboards.asset.MBDiscussionAssetRendererFactory;
import com.liferay.portlet.messageboards.asset.MBMessageAssetRendererFactory;
import com.liferay.portlet.messageboards.trash.MBCategoryTrashHandler;
import com.liferay.portlet.messageboards.trash.MBMessageTrashHandler;
import com.liferay.portlet.messageboards.trash.MBThreadTrashHandler;
import com.liferay.portlet.messageboards.util.MBMessageIndexer;
import com.liferay.portlet.messageboards.workflow.MBDiscussionWorkflowHandler;
import com.liferay.portlet.messageboards.workflow.MBMessageWorkflowHandler;
import com.liferay.portlet.trash.util.TrashIndexer;
import com.liferay.portlet.usersadmin.util.ContactIndexer;
import com.liferay.portlet.usersadmin.util.OrganizationIndexer;
import com.liferay.portlet.usersadmin.util.UserIndexer;
import com.liferay.portlet.wiki.asset.WikiPageAssetRendererFactory;
import com.liferay.portlet.wiki.trash.WikiNodeTrashHandler;
import com.liferay.portlet.wiki.trash.WikiPageTrashHandler;
import com.liferay.portlet.wiki.util.WikiNodeIndexer;
import com.liferay.portlet.wiki.util.WikiPageIndexer;
import com.liferay.portlet.wiki.workflow.WikiPageWorkflowHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Roberto Díaz
 */
public class PortalRegisterTestUtil {

	protected static void registerAssetRendererFactories() {
		if (_ASSET_RENDER_FACTORIES != null) {
			throw new IllegalStateException("Already initialized");
		}

		_ASSET_RENDER_FACTORIES = new ArrayList<AssetRendererFactory>(
			_ASSET_RENDERER_FACTORY_CLASSES.length);

		for (Class<?> clazz : _ASSET_RENDERER_FACTORY_CLASSES) {
			try {
				AssetRendererFactory assetRendererFactory =
					(AssetRendererFactory)clazz.newInstance();

				assetRendererFactory.setClassName(
					assetRendererFactory.getClassName());

				_ASSET_RENDER_FACTORIES.add(assetRendererFactory);
			}
			catch (IllegalAccessException iae) {
				iae.printStackTrace();
			}
			catch (InstantiationException ie) {
				ie.printStackTrace();
			}
		}

		AssetRendererFactoryRegistryUtil.register(_ASSET_RENDER_FACTORIES);
	}

	protected static void registerIndexers() {
		for (Indexer indexer : _INDEXERS) {
			IndexerRegistryUtil.register(indexer);
		}
	}

	protected static void registerTrashHandlers() {
		TrashHandlerRegistryUtil.register(_TRASH_HANDLERS);
	}

	protected static void registerWorkflowHandlers() {
		WorkflowHandlerRegistryUtil.register(_WORKFLOW_HANDLERS);
	}

	protected static void unregisterAssetRendererFactories() {
		if (_ASSET_RENDER_FACTORIES == null) {
			throw new IllegalStateException("Non initialized factories");
		}

		AssetRendererFactoryRegistryUtil.unregister(_ASSET_RENDER_FACTORIES);
	}

	protected static void unregisterIndexers() {
		for (Indexer indexer : _INDEXERS) {
			IndexerRegistryUtil.unregister(indexer);
		}
	}

	protected static void unregisterTrashHandlers() {
		TrashHandlerRegistryUtil.unregister(_TRASH_HANDLERS);
	}

	protected static void unregisterWorkflowHandlers() {
		WorkflowHandlerRegistryUtil.unregister(_WORKFLOW_HANDLERS);
	}

	private static final Class<?>[] _ASSET_RENDERER_FACTORY_CLASSES = {
		BlogsEntryAssetRendererFactory.class,
		BookmarksEntryAssetRendererFactory.class,
		BookmarksFolderAssetRendererFactory.class,
		DDLRecordAssetRendererFactory.class,
		DLFileEntryAssetRendererFactory.class,
		DLFolderAssetRendererFactory.class,
		JournalArticleAssetRendererFactory.class,
		JournalFolderAssetRendererFactory.class,
		LayoutRevisionAssetRendererFactory.class,
		MBCategoryAssetRendererFactory.class,
		MBDiscussionAssetRendererFactory.class,
		MBMessageAssetRendererFactory.class, UserAssetRendererFactory.class,
		WikiPageAssetRendererFactory.class
	};

	private static final BaseIndexer[] _INDEXERS = {
		new AssetIndexer(), new BlogsIndexer(), new ContactIndexer(),
		new BookmarksEntryIndexer(), new BookmarksFolderIndexer(),
		new DDLIndexer(), new DLFileEntryIndexer(), new DLFolderIndexer(),
		new JournalArticleIndexer(), new JournalFolderIndexer(),
		new MBMessageIndexer(), new OrganizationIndexer(), new TrashIndexer(),
		new UserIndexer(), new WikiNodeIndexer(), new WikiPageIndexer()
	};

	private static final List<TrashHandler> _TRASH_HANDLERS =
		Arrays.<TrashHandler>asList(
			new BlogsEntryTrashHandler(), new DLFileEntryTrashHandler(),
			new DLFileShortcutTrashHandler(), new DLFolderTrashHandler(),
			new JournalArticleTrashHandler(), new MBCategoryTrashHandler(),
			new MBMessageTrashHandler(), new MBThreadTrashHandler(),
			new WikiNodeTrashHandler(), new WikiPageTrashHandler()
		);

	private static final List<WorkflowHandler> _WORKFLOW_HANDLERS =
		Arrays.<WorkflowHandler>asList(
			new BlogsEntryWorkflowHandler(), new DDLRecordWorkflowHandler(),
			new DLFileEntryWorkflowHandler(),
			new JournalArticleWorkflowHandler(),
			new MBDiscussionWorkflowHandler(), new MBMessageWorkflowHandler(),
			new UserWorkflowHandler(), new WikiPageWorkflowHandler()
		);

	private static List<AssetRendererFactory> _ASSET_RENDER_FACTORIES;

}