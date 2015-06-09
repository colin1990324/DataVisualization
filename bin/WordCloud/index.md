---
layout: page
title: "Whole Site Word Cloud"
date: 2015-06-07T09:06:02+08:00
modified:
excerpt:
image:
  feature:
---

<div id="chart" style="width: 100%; margin: 0px auto; position: relative;"></div>

<script src="lib/d3/d3.js"></script>
<script src="d3.layout.cloud.js"></script>

<script>
  var fill = d3.scale.category20();
  var myMap = new Map();
  myMap.set("github", 100);
myMap.set("virus", 100);
myMap.set("each", 100);
myMap.set("polt", 91);
myMap.set("理想", 82);
myMap.set("card", 73);
myMap.set("first", 64);
myMap.set("school", 55);
myMap.set("volunteer", 55);
myMap.set("products", 55);
myMap.set("northwind", 55);
myMap.set("公司", 46);
myMap.set("problems", 46);
myMap.set("斧子", 46);
myMap.set("sick", 46);
myMap.set("cards", 37);
myMap.set("feet", 37);
myMap.set("climbing", 37);
myMap.set("女孩", 37);
myMap.set("product", 37);
myMap.set("magician", 37);
myMap.set("people", 37);
myMap.set("question", 37);
myMap.set("probability", 37);
myMap.set("following", 37);
myMap.set("names", 37);
myMap.set("test", 28);
myMap.set("width", 28);
myMap.set("data", 28);
myMap.set("next", 28);
myMap.set("simple", 28);
myMap.set("missouri", 28);
myMap.set("父亲", 28);
myMap.set("童话", 28);
myMap.set("services", 28);
myMap.set("example", 28);
myMap.set("现实", 28);
myMap.set("letters", 28);
myMap.set("university", 28);
myMap.set("solution", 28);
myMap.set("vaccinated", 28);
myMap.set("matches", 28);
myMap.set("havasupai", 28);
myMap.set("water", 28);
myMap.set("youtube", 28);
myMap.set("given", 28);
myMap.set("little", 19);
myMap.set("point", 19);
myMap.set("query", 19);
myMap.set("coding", 19);
myMap.set("pattern", 19);
myMap.set("climb", 19);
myMap.set("more", 19);
myMap.set("second", 19);
myMap.set("sample", 19);
myMap.set("政治家", 19);
myMap.set("same", 19);
myMap.set("free", 19);
myMap.set("need", 19);
myMap.set("around", 19);
myMap.set("microsoft", 19);
myMap.set("really", 19);
myMap.set("theme", 19);
myMap.set("valley", 19);
myMap.set("活", 19);
myMap.set("many", 19);
myMap.set("competition", 19);
myMap.set("事", 19);
myMap.set("gopro", 19);
myMap.set("孩儿", 19);
myMap.set("good", 19);
myMap.set("best", 19);
myMap.set("entities", 19);
myMap.set("arrangement", 19);
myMap.set("beautiful", 19);
myMap.set("人家", 19);
myMap.set("故事", 19);
myMap.set("possible", 19);
myMap.set("description", 19);
myMap.set("hiking", 19);
myMap.set("entity", 19);
myMap.set("video", 19);
myMap.set("discontinued", 10);
myMap.set("book", 10);
myMap.set("filter", 10);
myMap.set("rest", 10);
myMap.set("contain", 10);
myMap.set("program", 10);
myMap.set("开局", 10);
myMap.set("answers", 10);
myMap.set("google", 10);
myMap.set("service", 10);
myMap.set("用户", 10);
myMap.set("鸡毛", 10);
myMap.set("land", 10);
myMap.set("round", 10);
myMap.set("place", 10);
myMap.set("snow", 10);
myMap.set("mutation", 10);
myMap.set("starcraft", 10);

var width = 860,
    height = 500;

  d3.layout.cloud().size([width, height])
      .words([
        "github","virus","each","polt","理想","card","first","school","volunteer","products",
        "northwind","公司","problems","斧子","sick","cards","feet","climbing","女孩","product",
        "magician","people","question","probability","following","names","test","width","data","next",
        "simple","missouri","父亲","童话","services","example","现实","letters","university","solution",
        "vaccinated","matches","havasupai","water","youtube","given","little","point","query","coding",
        "pattern","climb","more","second","sample","政治家","same","free","need","around",
        "microsoft","really","theme","valley","活","many","competition","事","gopro","孩儿",
        "good","best","entities","arrangement","beautiful","人家","故事","possible","description","hiking",
        "entity","video","discontinued","book","filter","rest","contain","program","开局","answers",
        "google","service","用户","鸡毛","land","round","place","snow","mutation","starcraft"].map(function(d) {
        return {text: d, size: myMap.get(d)};
      }))
      .padding(5)
      .rotate(function() { return ~~(Math.random() * 5) * 30 - 60; })
      .font("Impact")
      .fontSize(function(d) { return d.size; })
      .on("end", draw)
      .start();

  function draw(words) {
    d3.select("#chart").append("svg")
        .attr("width", width)
        .attr("height", height)
      .append("g")
        .attr("transform", "translate(" + (width / 2 -30) + "," + (height / 2 + 10) + ")")
      .selectAll("text")
        .data(words)
      .enter().append("text")
        .style("font-size", function(d) { return d.size + "px"; })
        .style("font-family", "Impact")
        .style("fill", function(d, i) { return fill(i); })
        .attr("text-anchor", "middle")
        .attr("transform", function(d) {
          return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
        })
        .text(function(d) { return d.text; });
  }
</script>