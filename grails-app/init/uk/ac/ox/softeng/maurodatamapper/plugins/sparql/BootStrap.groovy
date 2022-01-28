/*
 * Copyright 2020-2022 University of Oxford and Health and Social Care Information Centre, also known as NHS Digital
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package uk.ac.ox.softeng.maurodatamapper.plugins.sparql

/*
 * Copyright 2020 University of Oxford and Health and Social Care Information Centre, also known as NHS Digital
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import uk.ac.ox.softeng.maurodatamapper.core.authority.AuthorityService
import uk.ac.ox.softeng.maurodatamapper.core.container.Folder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource

class BootStrap {

    @Autowired
    MessageSource messageSource

    AuthorityService authorityService

    def init = { servletContext ->
        environments {
            test {
                Folder.withNewTransaction {
/*
                    Folder folder = Folder.findByLabel('Functional Test Folder')
                    if(!folder)
                    {
                        folder = new Folder(label: 'Functional Test Folder', createdBy: StandardEmailAddress.getFUNCTIONAL_TEST())
                        checkAndSave(messageSource, folder)
                    }
                    Authority authority = authorityService.getDefaultAuthority()
                    if (DataModel.countByLabel(BootstrapModels.COMPLEX_DATAMODEL_NAME) == 0) {
                        BootstrapModels.buildAndSaveComplexDataModel(messageSource, folder, authority)
                    }
                    if (DataModel.countByLabel(BootstrapModels.SIMPLE_DATAMODEL_NAME) == 0) {
                        BootstrapModels.buildAndSaveSimpleDataModel(messageSource, folder, authority)
                    }

 */
                }
            }
        }
    }
    def destroy = {
    }
}
