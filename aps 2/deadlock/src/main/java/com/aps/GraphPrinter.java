package com.aps;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.rough.FillStyle;
import guru.nidi.graphviz.rough.Roughifyer;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;

public class GraphPrinter {
    public static void printGraph(Map<NoGrafo, Set<NoGrafo>> grafo) throws IOException {
        List<LinkSource> nodes = new ArrayList<>();

        for (Map.Entry<NoGrafo, Set<NoGrafo>> entry : grafo.entrySet()) {
            NoGrafo from = entry.getKey();
            Node fromNode = node(from.getIdentificador());
            
            Set<NoGrafo> targets = entry.getValue();
            if (targets != null && !targets.isEmpty()) {
                for (NoGrafo to : targets) {
                    fromNode = fromNode.link(node(to.getIdentificador()));
                }
            }
            nodes.add(fromNode);
        }

        Graph g = graph("grafo").directed().with(nodes).cluster();
        Graphviz viz = Graphviz.fromGraph(g);

        viz.render(Format.PNG).toFile(new File("grafos-gerados/grafo.png"));
    }

    public static void printGraphClustered(Map<NoGrafo, Set<NoGrafo>> grafo) throws IOException {
        List<LinkSource> processNodes = new ArrayList<>();
        List<LinkSource> resourceNodes = new ArrayList<>();
        List<LinkSource> relationNodes = new ArrayList<>();

        for (Map.Entry<NoGrafo, Set<NoGrafo>> entry : grafo.entrySet()) {
            NoGrafo from = entry.getKey();
            Node fromNode = node(from.getIdentificador());
            if (from instanceof Processo) processNodes.add(fromNode);
            if (from instanceof Conta) resourceNodes.add(fromNode);
            
            Set<NoGrafo> targets = entry.getValue();
            if (targets != null && !targets.isEmpty()) {
                for (NoGrafo to : targets) {
                    fromNode = fromNode.link(node(to.getIdentificador()));
                }
            }
            relationNodes.add(fromNode);
        }

        processNodes.sort((a1, a2) -> {
            int a1Number = Integer.parseInt(a1.name().value().replace("P", "").replace("R", ""));
            int a2Number = Integer.parseInt(a2.name().value().replace("P", "").replace("R", ""));
            return a2Number - a1Number;
        });

        relationNodes.sort((a1, a2) -> {
            int a1Number = Integer.parseInt(a1.name().value().replace("P", "").replace("R", ""));
            int a2Number = Integer.parseInt(a2.name().value().replace("P", "").replace("R", ""));
            return a2Number - a1Number;
        });
        
        resourceNodes.sort((a1, a2) -> {
            int a1Number = Integer.parseInt(a1.name().value().replace("P", "").replace("R", ""));
            int a2Number = Integer.parseInt(a2.name().value().replace("P", "").replace("R", ""));
            return a2Number - a1Number;
        });

        final Graph g = graph("ex1").directed().with(
            graph().directed().cluster()
                    .nodeAttr().with(Style.FILLED, Color.WHITE)
                    .graphAttr().with(Style.FILLED, Color.LIGHTGREY, Label.of("processos"))
                    .with(processNodes),
            graph("x").directed().cluster()
                    .nodeAttr().with(Style.FILLED)
                    .graphAttr().with(Color.BLUE, Label.of("recursos"))
                    .with(resourceNodes)
        ).with(relationNodes);

        Graphviz viz = Graphviz.fromGraph(g);

        // Output as SVG and PNG
        viz.render(Format.PNG).toFile(new File("grafos-gerados/grafo.png"));
    }
}
