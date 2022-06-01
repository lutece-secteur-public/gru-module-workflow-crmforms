/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.crmforms.util.constants;

/**
 *
 * NotifyCRMConstants
 *
 */
public final class NotifyCRMConstants
{
    // CONSTANTS
    public static final String TASK_NOTIFY_CRM_KEY = "taskNotifyCRM";

    // BEANS
    public static final String BEAN_TASK_CONFIG_SERVICE = "workflow-crmforms.taskNotifyCRMConfigService";
    public static final String CRM_CLIENT_SERVICE_BEAN_NAME = "crmclient.crmClientService";
    public static final String NOTIFY_CRM_SERVICE_BEAN_NAME = "workflow-crmforms.notifyCRMService";
    public static final String TASK_INFO_PROVIDER_BEAN_NAME = "workflow-crmforms.notifyCRMTaskInfoProvider";

    // PROPERTIES
    public static final String PROPERTY_CRMCLIENT_REST_WEBAPP_URL = "crmclient.crm.rest.webapp.url";
    public static final String PROPERTY_ID_DEMAND_TYPE_CRM = "module.workflow.crmforms.crmDemandTypeId";

    // MARKS
    public static final String MARK_CONFIG = "config";
    public static final String MARK_MESSAGE = "message";
    public static final String MARK_STATUS = "status";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LOCALE = "locale";
    public static final String MARK_LIST_MARKERS = "listMarkers";

    // PARAMETERS
    public static final String PARAMETER_APPLY = "apply";

    /**
     * Private constructor
     */
    private NotifyCRMConstants( )
    {
    }
}
