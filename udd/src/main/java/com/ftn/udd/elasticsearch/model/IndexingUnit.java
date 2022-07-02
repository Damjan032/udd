package com.ftn.udd.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "iu", type = "iu", shards = 1, replicas = 0)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndexingUnit {

	@Id
	@Field(type = FieldType.Long, store = true, index = false)
	private Long id;
	
	@Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String firstName;
	
	@Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String lastName;
    
    @Field(type = FieldType.Text)
	private String educationDegree;
	
    @Field(type = FieldType.Text, index = false, store = true)
    private String basicInfo;
    
    @Field(type = FieldType.Nested)
	private CVIndexingUnit cv;
	
    @GeoPointField
	private GeoPoint geoPoint;
}
