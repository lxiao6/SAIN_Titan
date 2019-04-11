/*
 * Project.java
 * Copyright (c) 2009, Drexel University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Drexel University nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.drexel.cs.rise.titan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.drexel.cs.rise.DesignSpace.data.DependencyType;
import edu.drexel.cs.rise.DesignSpace.data.refProject;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.JGridTable.repoManager;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public final class Project
{
    /*
     * private static final Project instance = new Project();
     * public static final Project getInstance()
     * {
     * return instance;
     * }
     */

    private final List<ProjectListener> listeners;
    private final Set<Clustering> collapsed;
    private WeightedDigraph<String> structure_dependency = null;
    private WeightedDigraph<String> history_dependency = null;
    private WeightedDigraph<String> f2f_dependency = null;
    private Clustering cluster = null;
    private File clusterPath = null;
    private File currentDirectory = new File(".");
    private String projectName;
    private boolean modified = false;
    private boolean weighted = false;
    private boolean rowLabeled = true;

    private DependencyType dpTypes = new DependencyType(/* "DependencyConfig" */);

    private static Map<String, refProject> refInformation = null;
    private static repoManager repo = repoManager.getInstance();

    private refProject curProj = new refProject();

    private boolean quickdraw = true;
    private boolean history = false;
    private boolean f2f = false;

    private int historyDsmThreshold = 0;
    private double f2fDsmThreshold = 0;
    private int top = 10;
    private int scope = 10;
    private int cochange = 4;

    public Project()
    {
        listeners = new ArrayList<ProjectListener>();
        collapsed = new HashSet<Clustering>();
        setRefInformation(refProject.loadProjectInfo("data.csv"));
        loadRepo();
        System.out.println("Total reference " + refInformation.size());
    }

    public Project(Project proj)
    {

        listeners = new ArrayList<ProjectListener>();
        this.structure_dependency = proj.getStructureDependency();
        this.history_dependency = proj.getHistoryDependency();
        this.f2f_dependency = proj.getf2fDependency();
        this.collapsed = proj.collapsed;
        this.weighted = proj.weighted;
        this.rowLabeled = proj.rowLabeled;
        this.dpTypes = proj.dpTypes.copy();
        this.historyDsmThreshold = proj.historyDsmThreshold;
        this.f2fDsmThreshold = proj.f2fDsmThreshold;
        this.top = proj.top;
        this.scope = proj.scope;
        this.cochange = proj.cochange;
    }

    public final WeightedDigraph<String> getStructureDependency()
    {
        return structure_dependency;
    }

    public final void setStructureDependency(final WeightedDigraph<String> structure_dependency)
    {
        this.structure_dependency = structure_dependency;
        fireStructureDependencyChanged();
    }

    public WeightedDigraph<String> getf2fDependency()
    {
        return f2f_dependency;
    }

    public void setf2f_dependency(WeightedDigraph<String> f2f_dependency)
    {
        if (f2f_dependency != null) {
            if (f2f_dependency.vertices().size() == 0 || f2f_dependency.edges().size() == 0) {
                this.f2f_dependency = null;
            }
            else
                this.f2f_dependency = f2f_dependency;
        }
        else {
            this.f2f_dependency = null;
        }

        firef2fDependencyChanged();
    }

    public WeightedDigraph<String> getHistoryDependency()
    {
        return history_dependency;
    }

    public void setHistory_dependency(WeightedDigraph<String> history_dependency)
    {
        if (history_dependency != null) {
            if (history_dependency.vertices().size() == 0 || history_dependency.edges().size() == 0) {
                this.history_dependency = null;
            }
            else
                this.history_dependency = history_dependency;
        }
        else {
            this.history_dependency = null;
        }

        fireHistoryDependencyChanged();
    }

    public final Clustering getCluster()
    {
        return cluster;
    }

    public final void setCluster(final Clustering cluster)
    {
        this.cluster = cluster;
        fireClusterChanged();
    }

    public final void setCluster(final Clustering cluster, final File path)
    {
        this.cluster = cluster;
        this.clusterPath = path;
        fireClusterChanged();
    }

    public final Set<Clustering> getCollapsed()
    {
        return collapsed;
    }

    public final void clearCollapsed()
    {
        collapsed.clear();
    }

    public final File getClusterPath()
    {
        return clusterPath;
    }

    public final void setClusterPath(final File path)
    {
        this.clusterPath = path;
    }

    public final File getCurrentDirectory()
    {
        return currentDirectory;
    }

    public final void setCurrentDirectory(final File dir)
    {
        this.currentDirectory = dir;
    }

    public final boolean isModified()
    {
        return modified;
    }

    public final void setModified(boolean modified)
    {
        this.modified = modified;
        fireModified();
    }

    public final boolean isWeighted()
    {
        return weighted;
    }

    public final void setWeighted(boolean weighted)
    {
        this.weighted = weighted;
    }

    public final boolean isRowLabeled()
    {
        return rowLabeled;
    }

    public final void setRowLabeled(boolean rowLabeled)
    {
        this.rowLabeled = rowLabeled;
    }

    public final void addProjectListener(final ProjectListener list)
    {
        synchronized (listeners) {
            listeners.add(list);
        }
    }

    public final void removeProjectListener(final ProjectListener list)
    {
        synchronized (listeners) {
            listeners.remove(list);
        }
    }

    protected final void fireStructureDependencyChanged()
    {
        for (ProjectListener list : getCurrentListeners(listeners))
            list.structureDependencyChanged(this);
    }

    protected final void fireHistoryDependencyChanged()
    {
        for (ProjectListener list : getCurrentListeners(listeners))
            list.historyDependencyChanged(this);
    }

    protected final void firef2fDependencyChanged()
    {
        for (ProjectListener list : getCurrentListeners(listeners))
            list.f2fDependencyChanged(this);
    }

    protected final void fireClusterChanged()
    {
        for (ProjectListener list : getCurrentListeners(listeners))
            list.clusterChanged(this);
    }

    protected final void fireModified()
    {
        for (ProjectListener list : getCurrentListeners(listeners))
            list.modified(this);
    }

    protected static final <T> List<T> getCurrentListeners(final List<T> list)
    {
        synchronized (list) {
            return new ArrayList<T>(list);
        }
    }

    /*
     * public boolean isInherit() {
     * return inherit;
     * }
     * 
     * public void setInherit(boolean inherit) {
     * this.inherit = inherit;
     * }
     * 
     * public boolean isRealize() {
     * return realize;
     * }
     * 
     * public void setRealize(boolean realize) {
     * this.realize = realize;
     * }
     * 
     * public boolean isAggregate() {
     * return aggregate;
     * }
     * 
     * public void setAggregate(boolean aggregate) {
     * this.aggregate = aggregate;
     * }
     * 
     * public boolean isDepend() {
     * return depend;
     * }
     * 
     * public void setDepend(boolean depend) {
     * this.depend = depend;
     * }
     * 
     * public boolean isNested() {
     * return nested;
     * }
     * 
     * public void setNested(boolean nested) {
     * this.nested = nested;
     * }
     */

    public void initDP(String[] tp)
    {
        dpTypes = new DependencyType(tp);
    }

    public DependencyType getDPControl()
    {
        return this.dpTypes;
    }

    public String[] getDpTypes()
    {
        return dpTypes.types();
    }

    public void setDPControl(DependencyType dpControl)
    {

        this.dpTypes = new DependencyType(dpControl);

    }

    public String[] getSelectedDpTypes()
    {
        return dpTypes.getSelected();
    }

    public int getDpTypeIndex(String dpTp)
    {

        return dpTypes.get_type_index(dpTp);
    }

    public boolean isTypeSelected(String name)
    {
        return dpTypes.is_select(name);
    }

    public void setSelectType(String name, boolean v)
    {
        dpTypes.select(name, v);
    }

    public int numTypes()
    {
        return dpTypes.types().length;
    }

    public int getHistoryDsmThreshold()
    {
        return historyDsmThreshold;
    }

    public void setHistoryDsmThreshold(int threshold)
    {
        this.historyDsmThreshold = threshold;
    }

    public double getf2fDsmThreshold()
    {
        return f2fDsmThreshold;
    }

    public void setf2fDsmThreshold(double threshold)
    {
        this.f2fDsmThreshold = threshold;
    }

    public boolean isQuickdraw()
    {
        return quickdraw;
    }

    public void setQuickdraw(boolean quickdraw)
    {
        this.quickdraw = quickdraw;
    }

    public boolean isHistory()
    {
        return history;
    }

    public void setHistory(boolean history)
    {
        this.history = history;
    }

    public boolean isf2f()
    {
        return f2f;
    }

    public void setf2f(boolean f2f)
    {
        this.f2f = f2f;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public Map<String, refProject> getRefInformation()
    {
        return refInformation;
    }

    public void setRefInformation(Map<String, refProject> ref)
    {
        refInformation = ref;
    }

    public repoManager getRepo()
    {
        return repo;
    }

    public void loadRepo()
    {
        repo.loadIndexFile();
    }

    public int getTop()
    {
        return top;
    }

    public void setTop(int top)
    {
        this.top = top;
    }

    public int getScope()
    {
        return scope;
    }

    public void setScope(int scope)
    {
        this.scope = scope;
    }

    public int getCochange()
    {
        return cochange;
    }

    public void setCochange(int cochange)
    {
        this.cochange = cochange;
    }

    public refProject getCurProect()
    {
        return curProj;
    }

    public void setCurName(String name)
    {
        curProj = new refProject();
        curProj.setName(name);
    }

    public void setCurPC(double pc)
    {
        curProj.setPc(pc);
    }

    public void setCurIdl(int level, int depth, double smil, double gmil)
    {
        curProj.setArchLevel(level);
        curProj.setArchDepth(depth);
        curProj.setsMil(smil);
        curProj.setgMil(gmil);
    }

    public void setCurSize(int size)
    {
        System.out.println("size is " + size);
        curProj.setSize(size);
    }

}
