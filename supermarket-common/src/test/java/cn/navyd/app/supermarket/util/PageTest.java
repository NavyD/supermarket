package cn.navyd.app.supermarket.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("分页page测试")
public class PageTest {
    private Collection<Integer> data;
    private final int pageNumber = 0, pageSize = 15, totalRows = 20;

    @BeforeEach
    void preInit() {
        data = new ArrayList<>();
    }

    @DisplayName("测试构造参数")
    @Test
    void testConstruct() {
        for (int i = 0; i < pageSize; i++)
            data.add(i);
        // 正常构造
        new Page<>(pageNumber, pageSize, totalRows, data);
        // 允许data为空
        data.clear();
        new Page<>(pageNumber, pageSize, totalRows, data);

        assertThatThrownBy(() -> new Page<>(-1, pageSize, totalRows, data), "pageNumber < 0，异常")
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Page<>(pageNumber, -1, totalRows, data), "pageSize < 0异常")
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Page<>(pageNumber, pageSize, -1, data), "totalRows < 0异常")
                .isInstanceOf(IllegalArgumentException.class);
        data.clear();
        assertThat(new Page<>(pageNumber, pageSize, totalRows, data)).as("data为空，正常");

        final int dataSize = pageSize + 1;
        for (int i = 0; i < dataSize; i++)
            data.add(i);
        assertThatThrownBy(() -> new Page<>(pageNumber, pageSize, totalRows, data), "data.size %s > %s pagesize. 异常",
                data.size(), pageSize).isInstanceOf(IllegalArgumentException.class);
        
        data.clear();
        assertThatThrownBy(() -> new Page<>(2, pageSize, totalRows, data), "pagenumber %s 超出分页范围. ", pageNumber)
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void getTotalPagesTest() {
        assertThat(new Page<>(pageNumber, pageSize, totalRows, data).getTotalPages()).isEqualTo(2);
    }

    @Test
    void getOffsetTest() {
        final int pageNumber = 0, pageSize = 15, totalRows = 10;
        Page<Integer> page = new Page<>(pageNumber, pageSize, totalRows, data);
        // 不是最后一页
        int offset = pageSize * (pageNumber + 1);
        // 最后一页
        if ((pageNumber + 1) * pageSize > totalRows) {
            offset = pageSize * pageNumber + page.getData().size();
        }
        assertThat(page.getOffset()).isEqualTo(offset);
    }

    @Test
    void getDataTest() {
        int pageNumber = 0, pageSize = 2, totalRows = 10;
        for (int i = 0; i < pageSize; i++)
            data.add(i);
        Page<Integer> page = new Page<>(pageNumber, pageSize, totalRows, data);
        assertThat(page.getData()).isEqualTo(data);
    }
}
