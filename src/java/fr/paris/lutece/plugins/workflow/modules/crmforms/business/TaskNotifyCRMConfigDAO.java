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
package fr.paris.lutece.plugins.workflow.modules.crmforms.business;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.crmforms.service.CRMFormsPlugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 *
 * TaskNotifyCRMConfigDAO
 *
 */
public class TaskNotifyCRMConfigDAO implements ITaskConfigDAO<TaskNotifyCRMConfig>
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_task, status_text, sender, object, message, is_demand_crm_creation FROM workflow_task_notify_crm_forms_cf  WHERE id_task = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO workflow_task_notify_crm_forms_cf( id_task, status_text, sender, object, message, is_demand_crm_creation ) VALUES ( ?,?,?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_notify_crm_forms_cf SET id_task = ?, status_text = ?,sender = ?, object = ?, message = ?, is_demand_crm_creation = ? WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM workflow_task_notify_crm_forms_cf WHERE id_task = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( TaskNotifyCRMConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, CRMFormsPlugin.getPlugin( ) ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            daoUtil.setString( nIndex++, config.getStatusText( ) );
            daoUtil.setString( nIndex++, config.getSender( ) );
            daoUtil.setString( nIndex++, config.getObject( ) );
            daoUtil.setString( nIndex++, config.getMessage( ) );
            daoUtil.setBoolean( nIndex++, config.getDemandCRMCreation( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TaskNotifyCRMConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, CRMFormsPlugin.getPlugin( ) ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            daoUtil.setString( nIndex++, config.getStatusText( ) );
            daoUtil.setString( nIndex++, config.getSender( ) );
            daoUtil.setString( nIndex++, config.getObject( ) );
            daoUtil.setString( nIndex++, config.getMessage( ) );
            daoUtil.setBoolean( nIndex++, config.getDemandCRMCreation( ) );

            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskNotifyCRMConfig load( int nIdTask )
    {
        TaskNotifyCRMConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, CRMFormsPlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdTask );

            daoUtil.executeQuery( );

            int nIndex = 1;

            if ( daoUtil.next( ) )
            {
                config = new TaskNotifyCRMConfig( );
                config.setIdTask( daoUtil.getInt( nIndex++ ) );
                config.setStatusText( daoUtil.getString( nIndex++ ) );
                config.setSender( daoUtil.getString( nIndex++ ) );
                config.setObject( daoUtil.getString( nIndex++ ) );
                config.setMessage( daoUtil.getString( nIndex++ ) );
                config.setDemandCRMCreation( daoUtil.getBoolean( nIndex++ ) );
            }
        }
        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, CRMFormsPlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeUpdate( );
        }
    }
}
