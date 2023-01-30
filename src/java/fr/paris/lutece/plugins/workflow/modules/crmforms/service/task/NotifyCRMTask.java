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
package fr.paris.lutece.plugins.workflow.modules.crmforms.service.task;

import fr.paris.lutece.plugins.crmclient.service.ICRMClientService;
import fr.paris.lutece.plugins.crmclient.util.CRMException;
import fr.paris.lutece.plugins.workflow.modules.crmforms.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.crmforms.service.INotifyCRMService;
import fr.paris.lutece.plugins.workflow.modules.crmforms.service.taskinfo.NotifyCRMTaskInfoProvider;
import fr.paris.lutece.plugins.workflow.modules.crmforms.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 *
 * NotifyCRMService
 *
 */
public class NotifyCRMTask extends SimpleTask
{
    @Inject
    @Named( NotifyCRMConstants.BEAN_TASK_CONFIG_SERVICE )
    private ITaskConfigService _taskConfigService;
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject 
    private INotifyCRMService _notifyCRMService;

    private static final String TASK_NOTIFY_CRM_TITLE = "Notifier Mon Compte";

    /**
     * Set the task config service
     * 
     * @param taskConfigService
     *            the task config service
     */
    public void setTaskConfigService( ITaskConfigService taskConfigService )
    {
        _taskConfigService = taskConfigService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return TASK_NOTIFY_CRM_TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
        _taskConfigService.remove( getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ICRMClientService crmClientService = (ICRMClientService) SpringContextService.getBean( NotifyCRMConstants.CRM_CLIENT_SERVICE_BEAN_NAME );
        INotifyCRMService notifyCrmService = (INotifyCRMService) SpringContextService.getBean( NotifyCRMConstants.NOTIFY_CRM_SERVICE_BEAN_NAME );
        NotifyCRMTaskInfoProvider taskProvider = (NotifyCRMTaskInfoProvider) SpringContextService.getBean( NotifyCRMConstants.TASK_INFO_PROVIDER_BEAN_NAME );

        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskNotifyCRMConfig notifyCRMConfig = (TaskNotifyCRMConfig) _taskConfigService.findByPrimaryKey( getId( ) );

        // Get infos for calling CRM Rest API
        int nIdResource = resourceHistory.getIdResource( );
        String strIdDemand = notifyCrmService.getIdDemand( nIdResource );
        String strUserGuid = notifyCrmService.getUserGuid( nIdResource );
        String strIdDemandType = AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_ID_DEMAND_TYPE_CRM );
        String strStatusText = notifyCRMConfig.getStatusText( );
        String strSender = notifyCRMConfig.getSender( );
        String strObject = replaceMarkersByValues( notifyCRMConfig.getObject( ), resourceHistory.getIdResource( )  );
        String strMessageFillWithUserInfos = taskProvider.getTaskResourceInfo( nIdResourceHistory, getId( ), request );

        if ( notifyCRMConfig.getDemandCRMCreation( ) )
        {
            // A new demand is created, with a first notification
            try
            {
                crmClientService.sendCreateDemandByUserGuidV2( strIdDemand, strIdDemandType, strUserGuid, "0", strStatusText, null, null );
                crmClientService.notifyV2( strIdDemand, strIdDemandType, strObject, strMessageFillWithUserInfos, strSender, null );
            }
            catch( CRMException e )
            {
                AppLogService.error( "NotifyCRMTask : unable to send creation demand ", e );
            }

        }
        else
        {
            // A existing demand is updated
            try
            {
                crmClientService.sendUpdateDemandV2( strIdDemand, strIdDemandType, strStatusText, null, "0", null );
                crmClientService.notifyV2( strIdDemand, strIdDemandType, strObject, strMessageFillWithUserInfos, strSender, null );
            }
            catch( CRMException e )
            {
                AppLogService.error( "NotifyCRMTask : unable to send updating demand ", e );
            }
        }
    }  
    
    /**
     * Method to replace markers by values
     * @param strContent
     * @param nIdRessource
     * @return 
     */
    private String replaceMarkersByValues( String strContent, int nIdRessource )
    {
        Map<String, Object> model = _notifyCRMService.fillModelInfoResource( nIdRessource );
        
        if( StringUtils.isNotEmpty( strContent ) && !model.isEmpty( ) )
        {
            for( Map.Entry<String, Object> resource: model.entrySet( ) )
            {
                strContent = strContent.replace( "${" + resource.getKey( ) + "!}", String.valueOf( resource.getValue( ) ) );
            }
        }
        
        return strContent;
    }
}
