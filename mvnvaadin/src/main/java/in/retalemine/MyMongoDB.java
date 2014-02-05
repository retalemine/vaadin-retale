package in.retalemine;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class MyMongoDB {
	private DB db;
	
	public MyMongoDB() {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			db = mongoClient.getDB( "retalemine" );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public String printCollections(){
		Set<String> colls = db.getCollectionNames();
		return colls.toString();
	}
	
	public void updateProductSet(){
		/*BasicDBObject queryDoc = new BasicDBObject("pName", "Lux Sandal").
                append("pUnit", new BasicDBObject("val", "big").append("unit", "size"));*/
		BasicDBObject queryDoc = new BasicDBObject("pName", "Lux Sandal").
                append("pUnit.val", "small").append("pUnit.unit", "size");
		BasicDBObject valDoc = new BasicDBObject("$addToSet", new BasicDBObject(
				"pPrice",new BasicDBObject("price",25).append("unit", "Rs"))).
				append("$set", new BasicDBObject("dateCM", new Date()));
		System.out.println(queryDoc);
		System.out.println(valDoc);
		update("productSetTest",queryDoc, valDoc);
	}
	
	private void update(String collectionName, BasicDBObject queryDoc, BasicDBObject valDoc){
		Boolean upsert = true, multi = true;
		WriteConcern concern = WriteConcern.JOURNALED;
		DBCollection dbCollection = db.getCollection(collectionName);
		dbCollection.update(queryDoc, valDoc, upsert, multi, concern );
	}
	
}
