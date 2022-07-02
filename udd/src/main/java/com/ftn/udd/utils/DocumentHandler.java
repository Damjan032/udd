package com.ftn.udd.utils;

import java.io.File;

import com.ftn.udd.elasticsearch.model.IndexingUnit;

public abstract class DocumentHandler {
	public abstract IndexingUnit getIndexingUnit(File file);
	public abstract String getText(File file);

}
