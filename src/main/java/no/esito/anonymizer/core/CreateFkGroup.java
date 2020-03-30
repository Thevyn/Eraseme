/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import no.esito.anonymizer.IDistribution;

public class CreateFkGroup {

    private IDistribution distribution;

    private CreateParent[] parents;

    public CreateFkGroup(IDistribution distribution, CreateParent[] parents) {
        this.distribution = distribution;
        this.parents = parents;
    }

    public CreateParent[] getParents() {
        return parents;
    }

    public IDistribution getDistribution() {
        return distribution;
    }

}
