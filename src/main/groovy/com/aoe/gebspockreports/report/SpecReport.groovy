/**
 * Copyright 2017 - 2019 Tilman Ginzel, AOE GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aoe.gebspockreports.report

class SpecReport {

    String label
    List<FeatureReport> features
    List<GebArtifact> unassignedArtifacts

    SpecReport() {
        features = new ArrayList<>()
        unassignedArtifacts = new ArrayList<>()
    }

    FeatureReport findFeatureByNumber(int number) {
        features.find { feature -> feature.number == number}
    }

    /**
     * Used while creating the spock-report template.
     *
     * @param number - feature number
     * @param featureName - the name provided by spock-reports during template creation
     * @return FeatureReport
     */
    FeatureReport findFeatureByNumberAndName(int number, String featureName) {
        // If @Unroll annotation is used, each feature run is suffixed with an index [n].
        // Unfortunately, the featureName parameter and feature.label use a slightly different pattern for this index.
        // We just remove the index and leading space to be able to find the correct feature.
        def featureNameWithoutIndex = featureName.replaceAll(/\s+\[\d+]$/, "")

        features.find { feature ->
            feature.number == number && feature.label.startsWith(featureNameWithoutIndex)
        }
    }

    /**
     * Used in html report to show all artifacts which could not be mapped to a specific feature/test method.
     * This is possible when the report label is invalid or reports where taken outside of feature methods,
     * e.g. setupSpec() or cleanupSpec() methods.
     *
     * @return List<GebArtifact>
     */
    List<GebArtifact> getUnassignedGebArtifacts() {
        List<FeatureReport> featuresNotAddedToReport = features.findAll { feature ->
            !feature.addedToReport
        }

        featuresNotAddedToReport.collectMany { feature -> feature.artifacts } + unassignedArtifacts
    }

    GebArtifact findUnassignedArtifactByLabel(String label) {
        unassignedArtifacts.find { artifact -> artifact.label == label }
    }
}
