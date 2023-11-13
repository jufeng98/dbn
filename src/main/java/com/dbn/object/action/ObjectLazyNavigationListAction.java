package com.dbn.object.action;

import com.dbn.common.util.Commons;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.list.DBObjectNavigationList;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.AnAction;

import java.util.List;

import static com.dbn.common.util.Unsafe.cast;

public class ObjectLazyNavigationListAction extends ObjectListShowAction {
    private final DBObjectRef<DBObject> parentObject;
    private final DBObjectNavigationList<?> navigationList;

    public ObjectLazyNavigationListAction(DBObject parentObject, DBObjectNavigationList navigationList) {
        super(navigationList.getName() + "...", parentObject);
        this.parentObject = DBObjectRef.of(parentObject);
        this.navigationList = navigationList;
    }

    @Override
    public List<DBObject> getObjectList() {
        return cast(Commons.coalesce(
                () -> navigationList.getObjects(),
                () -> navigationList.getObjectsProvider().getObjects()));
    }

    @Override
    public String getTitle() {
        return navigationList.getName();
    }

    @Override
    public String getEmptyListMessage() {
        return "No " + navigationList.getName() + " found";
    }

    @Override
    public String getListName() {
        return navigationList.getName();
    }

/*    @Override
    public void actionPerformed(final AnActionEvent e) {
        new BackgroundTask(parentObject.getProject(), "Loading " + navigationList.getName(), false, true) {
            @Override
            public void execute(@NotNull ProgressIndicator progressIndicator) {
                final ObjectNavigationListActionGroup linksActionGroup =
                        new ObjectNavigationListActionGroup(parentObject, navigationList, true);

                new SimpleLaterInvocator() {
                    public void run() {
                        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                                navigationList.getName(),
                                linksActionGroup,
                                e.getDataContext(),
                                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                                true, null, 10);

                        Project project = ActionUtil.getProject();
                        popup.showInCenterOf(DatabaseBrowserManager.getInstance(project).getBrowserPanel().getTree());
                    }
                }.start();
            }
        }.start();
    }*/

    @Override
    protected AnAction createObjectAction(DBObject object) {
        DBObject sourceObject = DBObjectRef.ensure(parentObject);
        return new NavigateToObjectAction(sourceObject, object);
    }
}
