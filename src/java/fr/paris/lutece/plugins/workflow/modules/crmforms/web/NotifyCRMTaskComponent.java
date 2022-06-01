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
package fr.paris.lutece.plugins.workflow.modules.crmforms.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.workflow.modules.crmforms.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.crmforms.service.INotifyCRMService;
import fr.paris.lutece.plugins.workflow.modules.crmforms.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 *
 * NotifyCRMTaskComponent
 *
 */
public class NotifyCRMTaskComponent extends NoFormTaskComponent
{
    private static final String TEMPLATE_TASK_NOTIFY_CRM_CONFIG = "admin/plugins/workflow/modules/crmforms/task_notify_crm_config.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        // In case there are no errors, then the config is created/updated

        ITaskConfigService taskNotifyCRMConfigService = (ITaskConfigService) SpringContextService.getBean( NotifyCRMConstants.BEAN_TASK_CONFIG_SERVICE );
        TaskNotifyCRMConfig config = taskNotifyCRMConfigService.findByPrimaryKey( task.getId( ) );

        boolean bCreate = false;
        if ( config == null )
        {
            config = new TaskNotifyCRMConfig( );
            config.setIdTask( task.getId( ) );
            bCreate = true;
        }

        try
        {
            BeanUtils.populate( config, request.getParameterMap( ) );
        }
        catch( IllegalAccessException | InvocationTargetException e )
        {
            AppLogService.error( "NotifyCRMTaskComponent - Unable to fetch data from request", e );
        }

        String strApply = request.getParameter( NotifyCRMConstants.PARAMETER_APPLY );

        // Check if the AdminUser clicked on "Apply" or on "Save"
        if ( StringUtils.isEmpty( strApply ) )
        {
            // Check mandatory fields
            Set<ConstraintViolation<TaskNotifyCRMConfig>> constraintViolations = BeanValidationUtil.validate( config );

            if ( !constraintViolations.isEmpty( ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }
        }

        if ( bCreate )
        {
            taskNotifyCRMConfigService.create( config );
        }
        else
        {
            taskNotifyCRMConfigService.update( config );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        ITaskConfigService taskNotifyCRMConfigService = (ITaskConfigService) SpringContextService.getBean( NotifyCRMConstants.BEAN_TASK_CONFIG_SERVICE );
        INotifyCRMService notifyCRMService = (INotifyCRMService) SpringContextService.getBean( NotifyCRMConstants.NOTIFY_CRM_SERVICE_BEAN_NAME );

        Map<String, Object> model = new HashMap<>( );

        model.put( NotifyCRMConstants.MARK_CONFIG, taskNotifyCRMConfigService.findByPrimaryKey( task.getId( ) ) );
        model.put( NotifyCRMConstants.MARK_LIST_MARKERS, notifyCRMService.getListMarkers( request, task ) );
        model.put( NotifyCRMConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_CRM_CONFIG, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

}
