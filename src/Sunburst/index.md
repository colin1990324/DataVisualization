---
layout: page
title: "Zoomable Sunburst"
date: 2015-06-08T10:06:02+08:00
modified:
excerpt:
image:
  feature:
---

<style>

path {
  stroke: #fff;
  fill-rule: evenodd;
}


.tooltip {
  font-size: 14px;
  font-family: Arial, sans-serif;
}
</style>


<div id="chart" style="width: 100%; margin: 0px auto; position: relative;">
</div>


<script src="d3.v3.min.js"></script>

<script>

var width = 760,
    height = 500,
    radius = Math.min(width, height) / 2;

var x = d3.scale.linear()
    .range([0, 2 * Math.PI]);

var y = d3.scale.sqrt()
    .range([0, radius]);

var color = d3.scale.category20c();

var svg = d3.select("#chart").append("svg")
    .attr("width", width)
    .attr("height", height)
  .append("g")
    .attr("transform", "translate(" + width / 2 + "," + (height / 2) + ")");

var partition = d3.layout.partition()
    .value(function(d) { return d.value; });

var arc = d3.svg.arc()
    .startAngle(function(d) { return Math.max(0, Math.min(2 * Math.PI, x(d.x))); })
    .endAngle(function(d) { return Math.max(0, Math.min(2 * Math.PI, x(d.x + d.dx))); })
    .innerRadius(function(d) { return Math.max(0, y(d.y)); })
    .outerRadius(function(d) { return Math.max(0, y(d.y + d.dy)); });


  var tooltip = d3.select("#chart")
    .append("div")
    .attr("class", "tooltip")
    .style("position", "absolute")
    .style("z-index", "10")
    .style("opacity", 0);

  function format_number(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }


  function format_name(d) {
    var name = d.name;
        return  '<b>' + name + '</b><br> (' + format_number(d.value) + ')';
  }
  
d3.json("blog.json", function(error, root) {

  var path = svg.selectAll("path")
      .data(partition.nodes(root))
    .enter().append("path")
      .attr("d", arc)
      .style("fill", function(d) { return color((d.children ? d : d.parent).name); })
      .on("click", click)
       .on("mouseover", function(d) {
          tooltip.html(function() {
              var name = format_name(d);
              return name;
         });
          return tooltip.transition()
            .duration(50)
            .style("opacity", 0.9);
        })
        .on("mousemove", function(d) {
          return tooltip
            .style("top", (height / 2 + 30)+"px")
            .style("left", (width / 2 -40)+"px");
        })
        .on("mouseout", function(){return tooltip.style("opacity", 0);});

 

  function click(d) {
    path.transition()
      .duration(750)
      .attrTween("d", arcTween(d));
  }
});

d3.select(self.frameElement).style("height", height + "px");

// Interpolate the scales!
function arcTween(d) {
  var xd = d3.interpolate(x.domain(), [d.x, d.x + d.dx]),
      yd = d3.interpolate(y.domain(), [d.y, 1]),
      yr = d3.interpolate(y.range(), [d.y ? 20 : 0, radius]);
  return function(d, i) {
    return i
        ? function(t) { return arc(d); }
        : function(t) { x.domain(xd(t)); y.domain(yd(t)).range(yr(t)); return arc(d); };
  };
}

</script>