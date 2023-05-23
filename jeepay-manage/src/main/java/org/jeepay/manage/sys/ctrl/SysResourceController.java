package org.jeepay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.MenuTreeBuilder;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.SysResource;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/resource")
public class SysResourceController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long resourceId = getLongRequired(param, "resourceId");
        SysResource sysResource = rpcCommonService.rpcSysService.findResourceById(resourceId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(sysResource));
    }


    /**
     * 新增
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增系统菜单" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysResource sysResource = getObject(param, SysResource.class);
        int count = rpcCommonService.rpcSysService.addResource(sysResource);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改系统菜单" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysResource sysResource = getObject(param, SysResource.class);
        int count = rpcCommonService.rpcSysService.updateResource(sysResource);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysResource sysResource = getObject(param, SysResource.class);
        int count = rpcCommonService.rpcSysService.countResource(sysResource);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectResource((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), sysResource);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(sysResourceList, count));
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除系统菜单" )
    public ResponseEntity<?> delete(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String resourceIds = getStringRequired(param, "resourceIds");
        String[] ids = resourceIds.split(",");
        List<Long> rids = new LinkedList<>();
        for(String resourceId : ids) {
            if(NumberUtils.isDigits(resourceId)) rids.add(Long.parseLong(resourceId));
        }
        rpcCommonService.rpcSysService.batchDeleteResource(rids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/all")
    @ResponseBody
    public ResponseEntity<?> all(HttpServletRequest request) {
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.SYSTEM_MGR);
        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            MenuTreeBuilder.Node node = new MenuTreeBuilder.Node();
            node.setResourceId(sysResource.getResourceId());
            node.setTitle(sysResource.getTitle());
            node.setParentId(sysResource.getParentId());
            nodeList.add(node);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(MenuTreeBuilder.buildTree(nodeList)));
    }

}