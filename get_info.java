import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Arrays;
import java.util.List;
public class get_info {
    public static int innings_played(String name,MongoCollection<Document>  collection)
    {
        int count=0;
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("$or", Arrays.asList(
                    new Document("bowler", name),
                    new Document("batter", name)
                ))),
                new Document("$group", new Document("_id", "$ID")
                .append("count", new Document("$sum", 1)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        for (Document doc : result) {
    //System.out.println(doc);
            count++;
        }
        return count;
    }

    public static int total_runs(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("batter", name)),
            new Document("$group", new Document("_id", "$batter")
                    .append("total_runs", new Document("$sum", "$batsman_run")))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        Document next2 = result.iterator().next();
        int count=0;
        for (Document doc : result) {
            //System.out.println(doc);
                    count++;
                }
        if(count==0)
        {
            return 0;
        }
        return (int)next2.get("total_runs");
    }

    public static int centuries(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("batter", name)),
            new Document("$group", new Document("_id", "$ID")
                    .append("total_runs", new Document("$sum", "$batsman_run"))),
            new Document("$match", new Document("total_runs", new Document("$gte", 100)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        int count=0;
        for (Document doc : result) {
            //System.out.println(doc);
                    count++;
        }     
        return count;
    }

    public static int half_centuries(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("batter", name)),
            new Document("$group", new Document("_id", "$ID")
                    .append("total_runs", new Document("$sum", "$batsman_run"))),
            new Document("$match", new Document("total_runs", new Document("$gte", 50)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        int count=0;
        for (Document doc : result) {
            //System.out.println(doc);
                    count++;
        }     
        return count;
    }
    public static double strike_rate(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("batter", name)),
            new Document("$group", new Document("_id", null)
                    .append("total_runs", new Document("$sum", "$batsman_run"))
                    .append("total_balls", new Document("$sum", 1))),
            new Document("$project", new Document("_id", 0)
                    .append("total_runs", 1)
                    .append("total_balls", 1)
                    .append("average", new Document("$divide", Arrays.asList("$total_runs", "$total_balls")))));
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        Document next2 = result.iterator().next();
        for (Document doc : result) {
            //System.out.println(doc);
        }  

        return  100*(double)next2.get("average");
    }
    public static int high_score(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("batter", name)),
            new Document("$group", new Document("_id", "$ID")
                    .append("total_runs", new Document("$sum", "$batsman_run"))),
            new Document("$match", new Document("total_runs", new Document("$gte", 50)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        int ans=0;
        for (Document doc : result) {
            int a = (int)doc.get("total_runs");
            if(a>ans)
            {
                ans=a;
            }
        }  

        return  ans;
    }
    public static int total_fours(String name,MongoCollection<Document>  collection,int val)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("batter", name)
            .append("batsman_run", val)
            .append("non_boundary", 0)),
            new Document("$group", new Document("_id", null)
            .append("count", new Document("$sum", 1)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        Document next2 = result.iterator().next();
        return (int)next2.get("count");
    }
    public static int total_wickets(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("bowler", name)
            .append("isWicketDelivery", 1)
            .append("kind", new Document("$ne", "NA"))),
            new Document("$group", new Document("_id", "$bowler")
            .append("count", new Document("$sum", 1)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        //Document next2 = result.iterator().next();
        System.out.println(result.iterator().next());
        //return (int)next2.get("count");
        return 0;
    }

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("cricket");
        MongoCollection<Document> collection = database.getCollection("ipl");
        String name ="DA Warner";
        int innings_=innings_played(name,collection);
        int total_runs_=total_runs(name,collection);
        System.out.println("innings: "+innings_);
        System.out.println("total_runs:  "+total_runs_);
        System.out.println("average:  "+total_runs_/innings_);
        System.out.println("total_centuries:  "+centuries(name,collection));
        System.out.println("half_centuries:  "+half_centuries(name,collection));
        System.out.println("high_score:  "+high_score(name,collection));
        System.out.println("Strike_rate:  "+strike_rate(name,collection));
        System.out.println("fours: "+total_fours(name,collection,4));
        System.out.println("sixes: "+total_fours(name,collection,6));
        System.out.println("wickets: "+total_wickets(name,collection));
    }
}
