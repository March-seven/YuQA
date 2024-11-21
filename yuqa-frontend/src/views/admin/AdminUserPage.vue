<template>
  <a-form
    :model="formSearchParams"
    :style="{ marginBottom: '20px' }"
    layout="inline"
    @submit="doSearch"
  >
    <a-form-item field="userName" label="用户名">
      <a-input
        allow-clear
        v-model="formSearchParams.userName"
        placeholder="请输入用户名"
      />
    </a-form-item>
    <a-form-item field="userProfile" label="用户简介">
      <a-input
        allow-clear
        v-model="formSearchParams.userProfile"
        placeholder="请输入用户简介"
      />
    </a-form-item>
    <a-form-item>
      <a-button type="primary" html-type="submit" style="width: 100px">
        搜索
      </a-button>
    </a-form-item>
  </a-form>
  <a-table
    :columns="columns"
    :data="dataList"
    :pagination="{
      showTotal: true,
      pageSize: searchParams.pageSize,
      current: searchParams.current,
      total,
    }"
    @page-change="onPageChange"
  >
    <template #userAvatar="{ record }">
      <a-image
        :src="record.userAvatar"
        title=""
        description=""
        width="108"
        footer-position="outer"
        :preview-visible="visible"
        @preview-visible-change="
          () => {
            visible = false;
          }
        "
      >
        <template #extra>
          <div class="actions actions-outer">
            <span
              class="action"
              @click="
                () => {
                  visible = true;
                }
              "
              ><icon-eye
            /></span>
            <span class="action" @click="onDownLoad(record.userAvatar)"
              ><icon-download
            /></span>
            <a-tooltip content="A user’s avatar">
              <span class="action"><icon-info-circle /></span>
            </a-tooltip>
          </div>
        </template>
      </a-image>
    </template>
    <template #createTime="{ record }">
      {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
    </template>
    <template #updateTime="{ record }">
      {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
    </template>
    <template #optional="{ record }">
      <a-space>
        <a-button status="danger" @click="doDelete(record)">删除</a-button>
      </a-space>
    </template>
  </a-table>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import {
  deleteUserUsingPost,
  listUserByPageUsingPost,
} from "@/api/userController";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import {
  IconEye,
  IconDownload,
  IconInfoCircle,
} from "@arco-design/web-vue/es/icon";
import dayjs from "dayjs";

const formSearchParams = ref<API.UserQueryRequest>({});

// 初始化搜索条件
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const searchParams = ref<API.UserQueryRequest>({
  current: 1,
  pageSize: 10,
});
const dataList = ref<API.User[]>([]);
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listUserByPageUsingPost(searchParams.value);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败" + res.data.message);
  }
};

/**
 * 执行搜索
 */
const doSearch = () => {
  searchParams.value = {
    ...initSearchParams,
    ...formSearchParams.value,
  };
};

/**
 * 分页改变时，改变搜素条件，触发数据加载
 * @param page
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

/**
 * 删除用户
 * @param record
 */
const doDelete = async (record: API.User) => {
  if (!record.id) {
    return;
  }
  const res = await deleteUserUsingPost({
    id: record.id,
  });
  if (res.data.code === 0) {
    loadData();
  } else {
    message.error("删除失败" + res.data.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});

// 表格列配置
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "用户名",
    dataIndex: "userName",
  },
  {
    title: "用户头像",
    dataIndex: "userAvatar",
    slotName: "userAvatar",
  },
  {
    title: "用户简介",
    dataIndex: "userProfile",
  },
  {
    title: "权限",
    dataIndex: "userRole",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    slotName: "updateTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];

// Image 组件显示

const visible = ref(false);

// return {

//   visible,
const onDownLoad = (userAvatar: any) => {
  // console.log(userAvatar);
  // 创建一个临时的链接
  const link = document.createElement("a");
  link.href = userAvatar;
  link.download = userAvatar.split("/").pop(); // 使用 URL 中的文件名作为下载名称
  document.body.appendChild(link);
  link.click(); // 模拟点击下载
  document.body.removeChild(link); // 移除临时链接
};
</script>
<style scoped>
.actions {
  display: flex;
  align-items: center;
}
.action {
  padding: 5px 4px;
  font-size: 14px;
  margin-left: 12px;
  border-radius: 2px;
  line-height: 1;
  cursor: pointer;
}
.action:first-child {
  margin-left: 0;
}

.action:hover {
  background: rgba(0, 0, 0, 0.5);
}
.actions-outer {
  .action {
    &:hover {
      color: #ffffff;
    }
  }
}
</style>
