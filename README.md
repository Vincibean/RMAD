# RMAD
R Plugin, optimized for Big Data, for KNIME Environment.
R MAD Nodes originate from a Master’s Degree Thesis project, an idea of prof.
Fabio Stella of the University of Milan – Bicocca.
The aim of this project is to provide a set of powerful yet easy-to-use clustering
nodes for KNIME. For this purpose, we decided to take advantage of some of the
most efficient clustering functions provided by R; hence, R MAD Nodes should not
be considered an alternative R plugin, but a new set of (clustering) nodes.
R MAD Nodes allow users to avoid writing R code, only requiring few parameters
that the user can set thanks to an easy-to-use graphical interface, in accordance
with KNIME Visual Programming Language pattern; R MAD nodes will then take
these parameters to elaborate the appropriate R code and process it thanks to a
dedicated R binary executable.
# R MAD Nodes Architecture
The R MAD Nodes’ architecture consists of four parts:
1. R executable: a R binary executable, comprehensive of all necessary
packages;
2. A software layer to communicate with R: since the underlying structure of
KNIME basic R Snippet nodes, tasked with communicating with R, is fully
functional and well-tested, it was kept, applying only little modifications.
3. Preference Page: a (Windows) user can safely use the R binary file provided
with the R MAD Extension since this file comes with all the packages
required for the correct operation of the R MAD Nodes. Nevertheless, the
user is allowed to use a personalized R binary. This can be done thanks to
the specifically developed R MAD Preference Page.
4. R MAD Nodes: the upper layer of the software structure, these nodes
bundle all the Java files needed for their proper functioning; they display a
graphical interface through which users can set function parameters and
eventually provide a view of the results.
# Implemented Nodes
• Bayesian Agglomerative Clustering: clusters data saved in a matrix using an
additive linear model with disappearing random effects. Has built-in spikeand-slab
components which quantifies important variables for clustering.
• Clara: computes a "clara" object, a list representing a clustering of the data
into k clusters.
• Cluster Mix: uses Markov chain Monte Carlo draws of indicator variables
from a normal component mixture model to cluster observations based on
a similarity matrix.
• Cluster Optimal: searches for the optimal k-clustering of the dataset
through a hypothesis test formulated as a model selection problem.
• Conjugate Convex Functions: partitions a data set into convex sets using
conjugate convex functions.
• EPP to P-Value: converts the Empirical Posterior Probability (EPP) computed
into a frequentist p-value, which can then be used to assess the significance
of the alternative hypothesis.
• High Dimensional Data Clustering: a model-based clustering method. It uses
the Expectation - Maximisation algorithm to estimate the parameters of
the model.
• Minimum Spanning Tree Clustering: builds a minimum spanning tree for each
cluster, stopping when the nearest-neighbour distance rises above a
specified threshold. Returns a set of clusters and a set of outliers.
• MovMF: Fit mixtures of von Mises-Fisher Distributions.
• Parallel Model Based Clustering: uses model-based clustering (unsupervised)
for high dimensional and ultra large data, in a dis
