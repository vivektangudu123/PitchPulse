import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;

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
        
        int count=0;
        for (Document doc : result) {
                    count++;
            if(count>0)break;
        }
        if(count==0)
        {
            return 0;
        }
        Document next2 = result.iterator().next();
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

        int count=0;
        for (Document doc : result) {
                    count++;
            if(count>0)break;
            }
        if(count==0)
        {
            return 0;
        }
        Document next2 = result.iterator().next();
        return 100*(double)next2.get("average");
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
        int count=0;
        for (Document doc : result) {
                    count++;
            if(count>0)break;
            }
        if(count==0)
        {
            return 0;
        }
        Document next2 = result.iterator().next();
        return (int)next2.get("count");
    }
    public static int total_wickets(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("bowler", name)
            .append("isWicketDelivery", 1)
            .append("kind", new Document("$ne", "run out"))),
            new Document("$group", new Document("_id", "$bowler")
            .append("count", new Document("$sum", 1)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        int count=0;
        for (Document doc : result) {
                    count++;
            if(count>0)break;
            }
        if(count==0)
        {
            return 0;}   
        Document next2 = result.iterator().next();
        return (int)next2.get("count");
    }
    public static int wicket_5(String name,MongoCollection<Document>  collection,int val)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("bowler", name)),
            new Document("$group", new Document("_id", "$ID")
                .append("count", new Document("$sum", new Document("$cond", 
                    Arrays.asList(
                        new Document("$and", Arrays.asList(
                            new Document("$eq", Arrays.asList("$isWicketDelivery", 1)),
                            new Document("$ne", Arrays.asList("$kind", "run out"))
                        )),
                        1,
                        0
                    )
                )))
            ),
            new Document("$match", new Document("count", new Document("$gt", val)))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        int count=0;
        for (Document doc : result) {
           // System.out.println(doc);
                    count++;
            }
        if(count==0)
        {
            return 0;
        }
        Document next2 = result.iterator().next();
        return count;
    }
    public static double economy(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("bowler", name)),
            new Document("$group", new Document("_id", null)
                    .append("total_runs", new Document("$sum", "$total_run"))
                    .append("total_balls", new Document("$sum", 1))),
            new Document("$project", new Document("_id", 0)
                    .append("total_runs", 1)
                    .append("total_balls", 1)
                    .append("average", new Document("$divide", Arrays.asList("$total_runs", "$total_balls")))));
        AggregateIterable<Document> result = collection.aggregate(pipeline); 

        int count=0;
        for (Document doc : result) {
                    count++;
            if(count>0)break;
            }
        if(count==0)
        {
            return 0;
        }
        Document next2 = result.iterator().next();
        return 6*(double)next2.get("average");
    }
    public static int total_runs_bowler(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("bowler", name)),
            new Document("$group", new Document("_id", "$bowler")
                    .append("total_runs", new Document("$sum", "$total_run")))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        
        int count=0;
        for (Document doc : result) {
                    count++;
            if(count>0)break;
        }
        if(count==0)
        {
            return 0;
        }
        Document next2 = result.iterator().next();
        return (int)next2.get("total_runs");
    }
    public static int maidens(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            // new Document("$match", new Document("bowler", name)),
            // new Document("$group", new Document("_id", new Document("ID", "$ID").append("overs", "$overs"))
            //         .append("zero_runs_count", new Document("$sum", new Document("$cond", Arrays.asList(
            //                 new Document("$eq", Arrays.asList("$total_run", 0)), 1, 0
            //         )))))
            new Document("$match", new Document("bowler", name)),
            new Document("$group", new Document("_id", new Document("ID", "$ID").append("overs", "$overs"))
                                    .append("total_runs", new Document("$sum", "$total_run"))),
            new Document("$match", new Document("total_runs", 0)),
            new Document("$count", "count")
        );
        
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        
        for(Document doc:result)
        {
            Document next2 = result.iterator().next();
            return (int)next2.get("count");
        }  
        return 0;
    }
    public static int innings_played_team(String name,MongoCollection<Document>  collection,String ha)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document(ha, name)),
            new Document("$group", new Document("_id", "$ID")),
            new Document("$count", "count")
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        
        int count=0;
        for (Document doc : result) {
                    count++;
            if(count>0)break;
        }
        if(count==0)
        {
            return 0;
        }
        Document next2 = result.iterator().next();
        return (int)next2.get("count");
    }
    public static int highest_season(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("BattingTeam", name)),
            new Document("$group", new Document("_id", new Document("season", "$Season").append("ID", "$ID"))
                                    .append("count", new Document("$sum", "$total_run"))),
            new Document("$group", new Document("_id", "$_id.season")
                                    .append("highest_count", new Document("$max", "$count"))),
            new Document("$project", new Document("_id", 0)
                                    .append("season", "$_id")
                                    .append("highest_count", 1))
        );
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        
        int count=0;
        for (Document doc : result) {
            count++;
            
            if(count==0)
            {
                return 0;
            }
            if(count==1)
            {
                System.out.println("Season" +"   Highest Score");
            }
            System.out.println(doc.get("season")+"     "+doc.get("highest_count"));
        }
        return 0;

    }
    public static int head_to_head(String name,MongoCollection<Document>  collection)
    {
        List<Document> pipeline = Arrays.asList(
            new Document("$match", new Document("Team2", name)),
    new Document("$group", new Document("_id", "$Team1")
                            .append("count", new Document("$addToSet", "$ID"))),
    new Document("$project", new Document("count", new Document("$size", "$count")))
);
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        
        List<Document> pipeline2 = Arrays.asList(
            new Document("$match", new Document("Team1", name)),
            new Document("$group", new Document("_id", "$Team2")
                                    .append("count", new Document("$addToSet", "$ID"))),
            new Document("$project", new Document("count", new Document("$size", "$count")))
        );
        AggregateIterable<Document> result2 = collection.aggregate(pipeline2);
        int count=0;
        for (Document doc : result) {
            String team2 = doc.getString("_id");
            String countSet1 = (String) doc.get("Team2");
            int count1=(int)doc.get("count");

            for (Document doc1 : result2) {
                String team1 = doc1.getString("_id");
                String countSet2 = (String) doc1.get("Team1");
                int count2=(int)doc1.get("count");
                String s=String.valueOf(count1+count2);
                if(team1.equals(team2))
                {
                    System.out.println(team1+" "+s);
                }
            }
        }
        if(count==0)
        {
            return 0;
        }
        //Document next2 = result.iterator().next();
        return 0;
    }
    public static void main(String[] args) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("cricket");
        MongoCollection<Document> collection = database.getCollection("ipl");

        int fun=-1;
        int limit=10;
        int season=0;

        if(fun==-1){
        String name ="Mumbai Indians";
        int innings_=innings_played(name,collection);

        if(innings_!=0){
        System.out.println("innings: "+innings_);  
        int total_runs_=total_runs(name,collection);
        System.out.println("total_runs:  "+total_runs_);
        System.out.println("average:  "+total_runs_/innings_);
        System.out.println("total_centuries:  "+centuries(name,collection));
        System.out.println("half_centuries:  "+half_centuries(name,collection));
        System.out.println("high_score:  "+high_score(name,collection));
        System.out.println("Strike_rate:  "+strike_rate(name,collection));
        System.out.println("fours: "+total_fours(name,collection,4));
        System.out.println("sixes: "+total_fours(name,collection,6));
        System.out.println("wickets: "+total_wickets(name,collection));
        System.out.println("5_wicket_haul: "+ wicket_5(name,collection,4));
        System.out.println("4_wicket_haul: "+ wicket_5(name,collection,3));
        System.out.println("economy: "+ economy(name,collection));
        System.out.println("runs: "+ total_runs_bowler(name,collection));
        System.out.println("maidens: "+ maidens(name,collection));}

        else{
            int team_innings=innings_played_team(name,collection,"BattingTeam");
            if(team_innings!=0){
                System.out.println(" ");
        System.out.println("total_matches: "+ team_innings);
        System.out.println("Won: "+ innings_played_team(name,collection,"WinningTeam"));
        System.out.println(" ");
        highest_season(name,collection);
        System.out.println(" ");
        head_to_head(name,collection);}
        }}


        //total score
        if(fun==1)
        {
            List<Document> pipeline = new ArrayList<>();

            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            
            pipeline.add(new Document("$group", new Document("_id", "$batter")
                    .append("total_runs", new Document("$sum", "$batsman_run"))));
            pipeline.add(new Document("$sort", new Document("total_runs", -1)));
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
            for (Document doc : result) {
                System.out.println(doc.get("_id")+" "+doc.get("total_runs"));
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }
        if(fun==10)
        {
            String mapFunction = "function() {" +
            "    if (this.Season==2022) {" +
            "        emit(this.batter, this.batsman_run);" +
            "    }" +
            "}";
    
    String reduceFunction = "function(key, values) {" +
            "    return Array.sum(values);" +
            "}";
    MapReduceIterable<Document> result = collection.mapReduce(mapFunction, reduceFunction).sort(new Document("values", -1));
    
    List<Document> sortedResult = new ArrayList<>();
for (Document doc : result) {
    sortedResult.add(doc);
}
sortedResult.sort((doc1, doc2) -> {
    Double totalRuns1 = doc1.getDouble("value");
    Double totalRuns2 = doc2.getDouble("value");
    return Double.compare(totalRuns2, totalRuns1);
});
    
    int count=0;
    for (Document doc : sortedResult) {
        String batter = doc.getString("_id");
        Double totalRuns = doc.getDouble("value");
        System.out.println("Batter: " + batter + ", Total Runs: " + totalRuns);
        //System.out.println(doc);
        count++;
        if(count>10)
        {
            break;
        }
    }
        }
        //centuries
        if(fun==2)
        {

            List<Document> pipeline = new ArrayList<>();


            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            pipeline.add(new Document("$group", new Document("_id", new Document("batter", "$batter").append("ID", "$ID"))
            .append("total_runs", new Document("$sum", "$batsman_run"))));
            pipeline.add(new Document("$match", new Document("total_runs", new Document("$gte", 100))));
            pipeline.add(new Document("$group", new Document("_id", "$_id.batter")
            .append("count", new Document("$sum", 1))));
            pipeline.add(new Document("$sort", new Document("count", -1)));
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
        
            for (Document doc : result) {
                System.out.println(doc.get("_id")+"   "+doc.get("count"));
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }
        //half_centuries
        if(fun==3)
        {
            List<Document> pipeline = new ArrayList<>();


            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            pipeline.add(new Document("$group", new Document("_id", new Document("batter", "$batter").append("ID", "$ID"))
            .append("total_runs", new Document("$sum", "$batsman_run"))));
            pipeline.add(new Document("$match", new Document("total_runs", new Document("$gte", 50))));
            pipeline.add(new Document("$group", new Document("_id", "$_id.batter")
            .append("count", new Document("$sum", 1))));
            pipeline.add(new Document("$sort", new Document("count", -1)));
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
        
            for (Document doc : result) {
                System.out.println(doc.get("_id")+" "+doc.get("count"));
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }

        //strike rate
        if(fun==4)
        {
            List<Document> pipeline = new ArrayList<>();


            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            pipeline.add(new Document("$group", new Document("_id", new Document("batter", "$batter"))
                                         .append("total_runs", new Document("$sum", "$batsman_run"))
                                         .append("total_balls", new Document("$sum", 1))));
            pipeline.add(    new Document("$project", new Document("_id", 0)
                                         .append("batter", "$_id.batter")
                                         .append("ID", "$_id.ID")
                                         .append("batsman_run", "$total_runs")
                                         .append("ballnumber", "$total_balls")
                                         .append("batsman_run_per_ball", new Document("$divide", Arrays.asList("$total_runs", "$total_balls")))));
            pipeline.add(new Document("$match", new Document("ballnumber", new Document("$gte", 20))));
            pipeline.add(new Document("$sort", new Document("batsman_run_per_ball", -1)));
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
        
            for (Document doc : result) {
                Double aa= doc.getDouble("batsman_run_per_ball");
                String roundedNumber = decimalFormat.format(100*aa);
                System.out.println(doc.get("batter")+" "+roundedNumber);
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }
        //wickets
        if(fun==5)
        {
            List<Document> pipeline = new ArrayList<>();

            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            pipeline.add(new Document("$match", new Document("isWicketDelivery", 1).append("kind", new Document("$ne", "run out"))));
            pipeline.add(    new Document("$group", new Document("_id", "$bowler")
            .append("count", new Document("$sum", 1))));
            pipeline.add(new Document("$sort", new Document("count", -1)));
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
        
            for (Document doc : result) {
                System.out.println(doc.get("_id")+" "+doc.get("count"));
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }
        if(fun==6)
        {
            List<Document> pipeline = new ArrayList<>();

            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            pipeline.add( new Document("$match", new Document("batsman_run", 6).append("non_boundary", 0)));
            pipeline.add(new Document("$group", new Document("_id", "$batter")
                                     .append("count", new Document("$sum", 1))));
            pipeline.add(new Document("$sort", new Document("count", -1)) );
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
        
            for (Document doc : result) {
                System.out.println(doc.get("_id")+" "+doc.get("count"));
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }
        if(fun==7)
        {
            List<Document> pipeline = new ArrayList<>();

            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            pipeline.add( new Document("$match", new Document("batsman_run", 4).append("non_boundary", 0)));
            pipeline.add(new Document("$group", new Document("_id", "$batter")
                                     .append("count", new Document("$sum", 1))));
            pipeline.add(new Document("$sort", new Document("count", -1)) );
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
        
            for (Document doc : result) {
                System.out.println(doc.get("_id")+" "+doc.get("count"));
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }
        if(fun==8)
        {
            List<Document> pipeline = new ArrayList<>();

            if (season!=0) {
                pipeline.add(new Document("$match", new Document("Season", season)));
            }
            pipeline.add(new Document("$match", new Document("$and", Arrays.asList(
                new Document("player_out", new Document("$ne", "NA")),
                new Document("kind", new Document("$ne", "run out"))
            ))));
            pipeline.add( new Document("$group", new Document("_id", new Document("bowler", "$bowler").append("ID", "$ID"))
            .append("count", new Document("$sum", 1))));
            pipeline.add(new Document("$match", new Document("count", new Document("$gt", 4))));
            pipeline.add(new Document("$sort", new Document("count", -1)));

            AggregateIterable<Document> result = collection.aggregate(pipeline);
            int count=0;
        
            for (Document doc : result) {
                Document aa=(Document)doc.get("_id");
                System.out.println(aa.get("bowler")+" "+doc.get("count"));
                count++;
                if(count>limit)
                {
                    break;
                }
            }
        }
    }
}