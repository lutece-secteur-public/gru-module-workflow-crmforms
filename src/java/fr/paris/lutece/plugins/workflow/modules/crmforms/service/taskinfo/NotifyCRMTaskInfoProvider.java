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
package fr.paris.lutece.plugins.workflow.modules.crmforms.service.taskinfo;

import fr.paris.lutece.plugins.workflow.modules.crmforms.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.crmforms.service.INotifyCRMService;
import fr.paris.lutece.plugins.workflow.modules.crmforms.service.CRMFormsPlugin;
import fr.paris.lutece.plugins.workflow.modules.crmforms.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflow.service.taskinfo.AbstractTaskInfoProvider;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * NotifyCRMTaskInfoProvider
 *
 */
public class NotifyCRMTaskInfoProvider extends AbstractTaskInfoProvider
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFY_NOTIFICATION = "admin/plugins/workflow/modules/crmforms/task_notify_crm_notification.html";
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( NotifyCRMConstants.BEAN_TASK_CONFIG_SERVICE )
    private ITaskConfigService _taskNotifyCRMConfigService;
    @Inject
    private INotifyCRMService _notifyCRMService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPluginName( )
    {
        return CRMFormsPlugin.PLUGIN_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskResourceInfo( int nIdHistory, int nIdTask, HttpServletRequest request )
    {
        Locale locale = Locale.FRANCE;

        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdHistory );
        TaskNotifyCRMConfig config = _taskNotifyCRMConfigService.findByPrimaryKey( nIdTask );

        Map<String, Object> model = _notifyCRMService.fillModelInfoResource( resourceHistory.getIdResource( ) );
        model.put( NotifyCRMConstants.MARK_MESSAGE, config.getMessage( ) );

        HtmlTemplate t = AppTemplateService
                .getTemplateFromStringFtl( AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_NOTIFICATION, locale, model ).getHtml( ), locale, model );
        return t.getHtml( );
    }
}
