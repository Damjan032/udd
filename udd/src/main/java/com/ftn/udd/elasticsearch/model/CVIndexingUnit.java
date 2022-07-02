package com.ftn.udd.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;

@Document(indexName = "cv", type = "cv")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CVIndexingUnit {

	@Id
	@Field(type = FieldType.Long, store = true, index = false)
	private long id;

	@Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String content;

	@Field(type = FieldType.Text, index = false, store = true)
	private String path;

	@Override
	public String toString() {
		return "CVIndexingUnit [id=" + id + ", content=" + content + ", path=" + path + "]";
	}

}
